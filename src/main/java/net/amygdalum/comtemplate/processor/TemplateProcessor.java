package net.amygdalum.comtemplate.processor;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import net.amygdalum.comtemplate.engine.ComtemplateException;
import net.amygdalum.comtemplate.engine.ConfigurableTemplateLoader;
import net.amygdalum.comtemplate.engine.DefaultErrorHandler;
import net.amygdalum.comtemplate.engine.SilentTemplateInterpreter;
import net.amygdalum.comtemplate.engine.GlobalTemplates;
import net.amygdalum.comtemplate.engine.Messages;
import net.amygdalum.comtemplate.engine.ResolverRegistry;
import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateGroup;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.TemplateInterpreter;
import net.amygdalum.comtemplate.engine.TemplateLoader;
import net.amygdalum.comtemplate.engine.TemplateVariable;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;

public class TemplateProcessor {

	public static final String SOURCE = "source";
	public static final String TARGET = "target";

	private static final String LIBRARIES = "libraries";
	private static final String CLASSPATH = "classpath";
	private static final String EXTENSION = "extension";

	private Path source;
	private Path target;
	private TemplateLoader loader;

	private String extension;

	public TemplateProcessor(String source, String target, Properties properties) throws IOException {
		this.source = validPath(source);
		this.target = validTargetPath(target);
		this.loader = loaderFor(this.source, this.target, properties);
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
			TemplateProcessor processor = new TemplateProcessor(args[0], args[1], properties(args[0]));
			Messages.info("processing templates started");
			processor.run();
			Messages.info("processing templates finished");
		} catch (ArrayIndexOutOfBoundsException | FileNotFoundException e) {
			Messages.error("signature: java " + TemplateProcessor.class.getName() + " <source path> <target path>");
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
		TemplateInterpreter interpreter = new SilentTemplateInterpreter(loader, ResolverRegistry.defaultRegistry(), GlobalTemplates.defaultTemplates(), new DefaultErrorHandler());

		Set<String> messages = new HashSet<>();
		for (String templateFileName : findAllSources()) {
			try {
				String templateName = templateFileName.substring(0, templateFileName.length() - 4).replace(File.separatorChar, '.');
				TemplateDefinition main = loader.loadDefinition(templateName + "._main");
				if (main == null) {
					continue;
				} else if (main.getParameter("data") == null) {
					generateMain(templateFileName, main, interpreter);
				} else {
					generateProjection(templateFileName, main, interpreter);
				}
			} catch (ComtemplateException e) {
				String msg = e.getMessage();
				if (messages.add(msg)) {
					Messages.error("failed template generation:\n" + msg);
				}
			}
		}
	}

	protected List<String> findAllSources() throws IOException {
		return Files.walk(source)
			.filter(path -> Files.isRegularFile(path))
			.map(path -> source.relativize(path))
			.filter(path -> path.getFileName().toString().endsWith(".ctp"))
			.map(path -> path.toString())
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

				String evaluate = main.evaluate(interpreter, globalScope);
				Files.write(targetPath, evaluate.getBytes(StandardCharsets.UTF_8));
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
						String evaluate = main.evaluate(interpreter, globalScope, dataVar);

						Files.write(targetPath, evaluate.getBytes(StandardCharsets.UTF_8));
					} catch (IOException e) {
						Messages.error("failed generating from " + fileName + " to " + targetPath + ": " + e.getMessage());
					}
				}
			}
		} catch (IOException e) {
			Messages.error("failed generating to " + parentPath + ": " + e.getMessage());
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
