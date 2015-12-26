package com.almondtools.comtemplate.processor;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import com.almondtools.comtemplate.engine.ComtemplateException;
import com.almondtools.comtemplate.engine.ConfigurableTemplateLoader;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateLoader;

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
		return properties.getProperty(EXTENSION, ".html");
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
			System.err.println("generation of files failed: " + e.getMessage());
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

	public void run() throws IOException {
		List<String> templateFileNames = Files.walk(source)
			.filter(path -> Files.isRegularFile(path))
			.map(path -> source.relativize(path))
			.filter(path -> path.getFileName().toString().endsWith(".ctp") && !path.getFileName().toString().startsWith("_"))
			.map(path -> path.toString())
			.collect(toList());
		for (String templateFileName : templateFileNames) {
			try {
				String templateName = templateFileName.substring(0, templateFileName.length() - 4).replace(File.separatorChar,'.');
				Path targetPath = target.resolve(templateFileName.replace(".ctp", extension));
				TemplateDefinition main = loader.loadDefinition(templateName + ".main");
				Files.createDirectories(targetPath.getParent());
				Scope globalScope = new Scope(main, var(SOURCE, string(source.toString())), var(TARGET, string(source.toString())));
				String evaluate = main.evaluate(globalScope);
				Files.write(targetPath, evaluate.getBytes());
			} catch (ComtemplateException e) {
				System.err.println(e.getMessage());
			}
		}
		List<Path> otherfiles = Files.walk(source)
			.filter(path -> Files.isRegularFile(path))
			.map(path -> source.relativize(path))
			.filter(path -> !path.toString().endsWith(".ctp"))
			.collect(toList());
		for (Path file : otherfiles) {
			Path sourcePath = source.resolve(file);
			Path targetPath = target.resolve(file);
			Files.createDirectories(targetPath.getParent());
			Files.copy(sourcePath, targetPath, REPLACE_EXISTING);
		}
	}

}
