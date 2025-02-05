package net.amygdalum.comtemplate.processor;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

import net.amygdalum.comtemplate.engine.ComtemplateException;
import net.amygdalum.comtemplate.engine.ConfigurableTemplateLoader;
import net.amygdalum.comtemplate.engine.DefaultErrorHandler;
import net.amygdalum.comtemplate.engine.DefaultTemplateInterpreter;
import net.amygdalum.comtemplate.engine.GlobalTemplates;
import net.amygdalum.comtemplate.engine.InterpreterListener;
import net.amygdalum.comtemplate.engine.Messages;
import net.amygdalum.comtemplate.engine.ResolverRegistry;
import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateGroup;
import net.amygdalum.comtemplate.engine.TemplateGroupNotFoundException;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.TemplateInterpreter;
import net.amygdalum.comtemplate.engine.TemplateLoader;
import net.amygdalum.comtemplate.engine.TemplateVariable;
import net.amygdalum.comtemplate.engine.expressions.EvalTemplate;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;
import net.amygdalum.comtemplate.engine.templates.FileDependent;

public class TemplateWatcher {

    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    private static final String LIBRARIES = "libraries";
    private static final String CLASSPATH = "classpath";
    private static final String EXTENSION = "extension";

    private Path source;
    private Path target;
    private TemplateLoader loader;
    private Map<String, Node> nodes;

    private String extension;

    public TemplateWatcher(String source, String target, Properties properties) throws IOException {
        this.source = validPath(source);
        this.target = validTargetPath(target);
        this.loader = loaderFor(this.source, this.target, properties);
        this.nodes = new HashMap<>();
        this.extension = extensionFrom(properties);
    }

    private String extensionFrom(Properties properties) {
        return "." + properties.getProperty(EXTENSION, "html");
    }

    private TemplateLoader loaderFor(Path source, Path target, Properties properties) {
        List<Path> paths = libraries(properties);
        boolean useClassPath = useClasspath(properties);

        return new ConfigurableTemplateLoader()
            .withSource(source)
            .withClasspath(useClassPath)
            .forPaths(paths);
    }

    private List<Path> libraries(Properties properties) {
        String libraries = properties.getProperty(LIBRARIES, "");
        List<Path> paths = Stream.of(libraries.split(","))
            .filter(path -> !path.isEmpty())
            .map(path -> Paths.get(path))
            .filter(path -> Files.isDirectory(path))
            .collect(toList());
        return paths;
    }

    private boolean useClasspath(Properties properties) {
        String classpath = properties.getProperty(CLASSPATH);
        return Optional.ofNullable(classpath)
            .map(value -> Boolean.valueOf(value))
            .orElse(true);
    }

    public static Path validPath(String target) throws FileNotFoundException {
        Path path = Paths.get(target);
        if (!Files.exists(path)) {
            throw new FileNotFoundException(target);
        }
        return path;

    }

    public static Path validTargetPath(String target) throws IOException {
        Path path = Paths.get(target);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        } else if (!Files.isDirectory(path)) {
            throw new FileNotFoundException(target);
        }
        return path;
    }

    public static void main(String... args) {
        try {
            TemplateWatcher processor = new TemplateWatcher(args[0], args[1], properties(args[0]));
            Messages.info("template watcher started");
            processor.run();
            Messages.info("template watcher finished");
        } catch (ArrayIndexOutOfBoundsException | FileNotFoundException e) {
            Messages.error("signature: java " + TemplateWatcher.class.getName() + " <source path> <target path>");
        } catch (IOException e) {
            Messages.error("error setting up template processor " + e.getMessage());
        }
    }

    private static Properties properties(String source) {
        Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(Paths.get(source).resolve("template.properties")));
        } catch (IOException e) {
            // use defaults
        }
        return properties;
    }

    public void run() throws IOException {
        DefaultTemplateInterpreter interpreter = new DefaultTemplateInterpreter(loader, ResolverRegistry.defaultRegistry(), GlobalTemplates.defaultTemplates(), new DefaultErrorHandler());
        interpreter.addListener(new InterpreterListener() {

            private TemplateDefinition fetchTemplateDefinition(Scope scope, TemplateExpression source) {
                if (source instanceof EvalTemplate) {
                    EvalTemplate eval = (EvalTemplate) source;
                    String name = eval.getTemplate();
                    TemplateDefinition definition = eval.getDefinition();
                    return interpreter.resolveTemplate(definition, scope, name);
                } else {
                    return null;
                }

            }

            @Override
            public void notify(Scope scope, TemplateExpression source, TemplateImmediateExpression result) {
                TemplateDefinition def = fetchTemplateDefinition(scope, source);

                if (def instanceof FileDependent) {
                    for (Path path : ((FileDependent) def).files(interpreter, scope, source)) {
                        Path relativePath = TemplateWatcher.this.source.relativize(path);
                        while (scope.getParent() != null) {
                            scope = scope.getParent();
                        }
                        String sourceName = scope.getGroup().getName();
                        Node sourceNode = nodes.get(sourceName);
                        String includeName = relativePath.toString();
                        Node includeNode = nodes.get(includeName);
                        if (sourceNode == null) {
                            //should not occur, do nothing at first
                        } else {
                            if (includeNode == null) {
                                includeNode = new Node(includeName);
                                nodes.put(includeName, includeNode);
                                System.out.println("registered: " + includeName + ":" + nodes.get(includeName).name());
                            }
                            sourceNode.dependsOn(includeNode);
                        }
                    }

                }
            }
        });

        Set<String> messages = new HashSet<>();
        for (String templateFileName : findAllSources()) {
            try {
                String templateName = templateFileName.substring(0, templateFileName.length() - 4).replace(File.separatorChar, '.');
                TemplateDefinition main = loader.loadDefinition(templateName + "._main");
                if (main == null) {
                    continue;
                } else if (main.getParameter("data") == null) {
                    updateGraph(main.getGroup());
                    generateMain(templateFileName, main, interpreter);
                } else {
                    updateGraph(main.getGroup());
                    generateProjection(templateFileName, main, interpreter);
                }
            } catch (ComtemplateException e) {
                String msg = e.getMessage();
                if (messages.add(msg)) {
                    Messages.error("failed template generation:\n" + msg);
                }
            }
        }

        WatchService watcher = FileSystems.getDefault().newWatchService();
        for (Path path : findAllDirectories()) {
            path.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down template watcher...");
            try {
                watcher.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        while (true) {
            try {
                WatchKey key;
                key = watcher.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == OVERFLOW) {
                        continue;
                    }

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path dir = (Path) key.watchable();
                    Path filename = ev.context();
                    Path sourceRelativeFilename = source.relativize(dir.resolve(filename));

                    if (sourceRelativeFilename.toString().endsWith(".ctp")) {
                        System.out.println("Detected change in: " + filename);
                        regenerateDependents(sourceRelativeFilename.toString(), interpreter);
                    } else {
                        System.out.println("Detected change in: " + sourceRelativeFilename + ":" + nodes.get(sourceRelativeFilename.toString()));
                        regenerateIncludeDependents(sourceRelativeFilename.toString(), interpreter);
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private Node updateGraph(TemplateGroup group) {
        String name = group.getName();
        Node node = new Node(name);
        Node oldNode = nodes.get(name);
        if (oldNode != null) {
            oldNode.clearDependencies();
            oldNode.adoptProvisions(node);
        }
        nodes.put(name, node);
        for (TemplateDefinition def : group.getImports()) {
            TemplateGroup importGroup = def.getGroup();
            String importName = importGroup.getName();
            Node importNode = nodes.get(importName);
            if (importNode == null) {
                importNode = updateGraph(importGroup);
            }
            node.dependsOn(importNode);
        }
        //TODO track md references
        return node;
    }

    protected List<String> findAllSources() throws IOException {
        return Files.walk(source)
            .filter(path -> Files.isRegularFile(path))
            .map(path -> source.relativize(path))
            .filter(path -> path.getFileName().toString().endsWith(".ctp"))
            .map(path -> path.toString())
            .collect(toList());
    }

    protected List<Path> findAllDirectories() throws IOException {
        return Files.walk(source)
            .filter(path -> Files.isDirectory(path))
            .collect(toList());
    }

    protected void generateMain(String templateFileName, TemplateDefinition main, TemplateInterpreter interpreter) {
        Path parentPath = target.resolve(templateFileName).getParent();
        try {
            Files.createDirectories(parentPath);
            Scope globalScope = new Scope(main, var(SOURCE, string(source.toString())), var(TARGET, string(source.toString())));

            TemplateDefinition name = main.getGroup().getDefinition("_name");

            String fileName = name != null ? name.evaluate(interpreter, globalScope) : templateFileName.replace(".ctp", extension);
            Path targetPath = target.resolve(fileName);
            try {
                Messages.info("Generating " + targetPath);

                String evaluate = main.evaluate(interpreter, globalScope);
                Files.write(targetPath, evaluate.getBytes(StandardCharsets.UTF_8));
                Messages.info("Generated " + targetPath);
            } catch (IOException e) {
                Messages.error("failed generating from " + templateFileName + " to " + targetPath + ": " + e.getMessage());
            }
        } catch (IOException e) {
            Messages.error("failed generating to " + parentPath + ": " + e.getMessage());
        }
    }

    protected void generateProjection(String templateFileName, TemplateDefinition main, TemplateInterpreter interpreter) {
        Path parentPath = target.resolve(templateFileName).getParent();
        try {
            Files.createDirectories(parentPath);
            Scope globalScope = new Scope(main, var(SOURCE, string(source.toString())), var(TARGET, string(source.toString())));

            TemplateGroup group = main.getGroup();
            ResolvedListLiteral data = group.resolveVariable("data")
                .map(v -> v.getValue().apply(interpreter, globalScope))
                .filter(v -> v instanceof ResolvedListLiteral)
                .map(v -> (ResolvedListLiteral) v)
                .orElse(new ResolvedListLiteral());

            TemplateDefinition name = main.getGroup().getDefinition("_name");
            TemplateDefinition valid = main.getGroup().getDefinition("_valid");

            for (TemplateImmediateExpression dataItem : data.getList()) {

                TemplateVariable dataVar = var("data", dataItem);

                if (isValid(valid, interpreter, globalScope, dataVar)) {
                    String fileName = name.evaluate(interpreter, globalScope, dataVar);
                    Path targetPath = target.resolve(fileName);

                    try {
                        Messages.info("Generating " + targetPath);

                        String evaluate = main.evaluate(interpreter, globalScope, dataVar);

                        Files.write(targetPath, evaluate.getBytes(StandardCharsets.UTF_8));
                        Messages.info("Generated " + targetPath);
                    } catch (IOException e) {
                        Messages.error("failed generating from " + fileName + " to " + targetPath + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            Messages.error("failed generating to " + parentPath + ": " + e.getMessage());
        }
    }

    protected void regenerateDependents(String templateFileName, TemplateInterpreter interpreter) {
        String templateName = templateFileName.substring(0, templateFileName.length() - 4).replace(File.separatorChar, '.');
        TemplateGroup group = loader.loadGroup(templateName);
        Node node = updateGraph(group);

        Queue<Node> todo = new LinkedList<>();
        Set<Node> done = new HashSet<Node>();
        todo.add(node);
        while (!todo.isEmpty()) {
            Node current = todo.poll();
            if (done.contains(current)) {
                continue;
            }
            done.add(current);
            Set<Node> provisions = node.provisions();
            String mainTemplateName = current.name();
            TemplateDefinition main = loader.loadDefinition(mainTemplateName + "._main");
            if (main == null) {

            } else if (main.getParameter("data") == null) {
                generateMain(main.getGroup().getResource(), main, interpreter);
            } else {
                generateProjection(main.getGroup().getResource(), main, interpreter);
            }
            todo.addAll(provisions);
        }
    }

    protected void regenerateIncludeDependents(String sourceFile, TemplateInterpreter interpreter) {
        Node node = nodes.get(sourceFile);
        if (node == null) {
            return;
        }

        Queue<Node> todo = new LinkedList<>();
        Set<Node> done = new HashSet<Node>();
        todo.add(node);
        while (!todo.isEmpty()) {
            Node current = todo.poll();
            if (done.contains(current)) {
                continue;
            }
            done.add(current);
            Set<Node> provisions = node.provisions();
            String mainTemplateName = current.name();
            try {
                TemplateDefinition main = loader.loadDefinition(mainTemplateName + "._main");
                if (main == null) {

                } else if (main.getParameter("data") == null) {
                    generateMain(main.getGroup().getResource(), main, interpreter);
                } else {
                    generateProjection(main.getGroup().getResource(), main, interpreter);
                }
            } catch (TemplateGroupNotFoundException e) {
                // only template groups are generated so skip this case
            }
            todo.addAll(provisions);
        }
    }

    protected boolean isValid(TemplateDefinition valid, TemplateInterpreter interpreter, Scope globalScope, TemplateVariable dataVar) {
        if (valid == null) {
            return true;
        }
        TemplateImmediateExpression evaluated = valid.evaluate(interpreter, globalScope, asList(dataVar));
        if (evaluated == null) {
            return false;
        }
        Boolean result = evaluated.as(Boolean.class);
        if (result == null) {
            return false;
        }
        return result;
    }

}
