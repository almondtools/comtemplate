package com.almondtools.comtemplate.processor;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import com.almondtools.comtemplate.engine.ComtemplateException;
import com.almondtools.comtemplate.engine.ConfigurableTemplateLoader;
import com.almondtools.comtemplate.engine.DefaultErrorHandler;
import com.almondtools.comtemplate.engine.DefaultTemplateInterpreter;
import com.almondtools.comtemplate.engine.GlobalTemplates;
import com.almondtools.comtemplate.engine.ResolverRegistry;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateLoader;
import com.almondtools.comtemplate.engine.TemplateVariable;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;

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

	public static void main(String[] args) {
		try {
			TemplateProcessor processor = new TemplateProcessor(args[0], args[1], properties(args[0]));
			System.out.println("processing templates started");
			processor.run();
			System.out.println("processing templates finished");
		} catch (ArrayIndexOutOfBoundsException | FileNotFoundException e) {
			System.err.println("signature: java " + TemplateProcessor.class.getName() + " <source path> <target path>");
		} catch (IOException e) {
			System.err.println("error setting up template processor " + e.getMessage());
		} finally {
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

	public void run() {
		List<String> templateFileNames = findAllSources();

		TemplateInterpreter interpreter = new DefaultTemplateInterpreter(loader, ResolverRegistry.defaultRegistry(), GlobalTemplates.defaultTemplates(), new DefaultErrorHandler());
		for (String templateFileName : templateFileNames) {
			try {
				String templateName = templateFileName.substring(0, templateFileName.length() - 4).replace(File.separatorChar, '.');
				TemplateDefinition main = loader.loadDefinition(templateName + ".main");
				if (main == null) {
					continue;
				} else if (main.getParameter("data") == null) {
					generateMain(templateFileName, main, interpreter);
				} else {
					generateProjection(templateFileName, main, interpreter);
				}
			} catch (ComtemplateException e) {
				System.err.println("failed template generation");
				e.printStackTrace(System.err);
			}
		}
	}

	private List<String> findAllSources()  {
		try {
			return Files.walk(source)
				.filter(path -> Files.isRegularFile(path))
				.map(path -> source.relativize(path))
				.filter(path -> path.getFileName().toString().endsWith(".ctp") && !path.getFileName().toString().startsWith("_"))
				.map(path -> path.toString())
				.collect(toList());
		} catch (IOException e) {
			System.err.println("cannot collect source files from " + source);
			e.printStackTrace(System.err);
			return emptyList();
		}
	}

	private void generateMain(String templateFileName, TemplateDefinition main, TemplateInterpreter interpreter) {
		Path targetPath = target.resolve(templateFileName.replace(".ctp", extension));
		try {
			Files.createDirectories(targetPath.getParent());
			Scope globalScope = new Scope(main, var(SOURCE, string(source.toString())), var(TARGET, string(source.toString())));

			String evaluate = main.evaluate(interpreter, globalScope);
			Files.write(targetPath, evaluate.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			System.err.println("failed generating from " + templateFileName + " to " + targetPath);
			e.printStackTrace(System.err);
		}
	}

	private void generateProjection(String templateFileName, TemplateDefinition main, TemplateInterpreter interpreter) {
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

			TemplateDefinition name = main.getGroup().getDefinition("name");
			TemplateDefinition valid = main.getGroup().getDefinition("valid");

			for (TemplateImmediateExpression dataItem : data.getList()) {

				TemplateVariable dataVar = var("data", dataItem);

				if (isValid(valid, interpreter, globalScope, dataVar)) {
					String fileName = name.evaluate(interpreter, globalScope, dataVar);
					Path targetPath = target.resolve(fileName);

					try {
						String evaluate = main.evaluate(interpreter, globalScope, dataVar);

						Files.write(targetPath, evaluate.getBytes(StandardCharsets.UTF_8));
					} catch (IOException e) {
						System.err.println("failed generating from " + fileName + " to " + targetPath);
						e.printStackTrace(System.err);
					}
				}
			}
		} catch (IOException e) {
			System.err.println("failed generating to " + parentPath);
			e.printStackTrace(System.err);
		}
	}

	private boolean isValid(TemplateDefinition valid, TemplateInterpreter interpreter, Scope globalScope, TemplateVariable dataVar) {
		if (valid == null) {
			return true;
		}
		TemplateImmediateExpression evaluated = valid.evaluate(interpreter, globalScope, asList(dataVar));
		try {
			return evaluated.as(Boolean.class);
		} catch (NullPointerException e) {
			return false;
		}
	}

}
