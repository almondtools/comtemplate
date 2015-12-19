package com.almondtools.comtemplate.processor;

import static java.util.stream.Collectors.toList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import com.almondtools.comtemplate.engine.BasePathTemplateLoader;
import com.almondtools.comtemplate.engine.ClassPathTemplateLoader;
import com.almondtools.comtemplate.engine.CompositeTemplateLoader;
import com.almondtools.comtemplate.engine.DefaultTemplateCompiler;
import com.almondtools.comtemplate.engine.TemplateCompiler;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateLoader;

public class TemplateProcessor {

	private static final String IMPORTS = "imports";
	private static final String LIBRARY = "library";
	private static final String CLASSPATH = "classpath";
	private static final String EXTENSION = "extension";

	private Path source;
	private Path target;
	private TemplateLoader loader;

	private String extension;
	private List<String> imports;

	public TemplateProcessor(String source, String target, Properties properties) throws IOException {
		this.source = validPath(source);
		this.target = validTargetPath(target);
		this.loader = loaderFor(source, properties);
		this.extension = extensionFrom(properties);
		this.imports = importsFrom(properties);
	}

	private List<String> importsFrom(Properties properties) {
		String imports = properties.getProperty(IMPORTS, "");
		return Stream.of(imports.split(","))
			.filter(str -> !str.isEmpty())
			.collect(toList());
	}

	private String extensionFrom(Properties properties) {
		return properties.getProperty(EXTENSION, ".html");
	}

	private TemplateLoader loaderFor(String source, Properties properties) {
		String library = properties.getProperty(LIBRARY, CLASSPATH);
		if (CLASSPATH.equals(library)) {
			TemplateCompiler compiler = new DefaultTemplateCompiler();
			return new CompositeTemplateLoader(compiler, new ClassPathTemplateLoader(compiler), new BasePathTemplateLoader(compiler, source));
		} else if (Files.isDirectory(Paths.get(library))) {
			TemplateCompiler compiler = new DefaultTemplateCompiler();
			return new CompositeTemplateLoader(compiler, new BasePathTemplateLoader(compiler, library), new BasePathTemplateLoader(compiler, source));
		} else {
			return new BasePathTemplateLoader(source);
		}
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

	private static Properties properties(String source) throws IOException {
		Properties properties = new Properties();
		properties.load(Files.newBufferedReader(Paths.get(source).resolve("template.properties")));
		return properties;
	}

	public void run() throws IOException {
		List<TemplateDefinition> imports = loadImports();
		List<String> templateNames = Files.walk(source)
			.filter(path -> Files.isRegularFile(path))
			.map(path -> source.relativize(path))
			.map(path -> path.toString())
			.filter(path -> path.endsWith(".ctp"))
			.map(path -> path.substring(0, path.length() - 4))
			.collect(toList());
		for (String templateName : templateNames) {
			Path targetPath = target.resolve(templateName + extension);
			TemplateDefinition main = loader.loadMain(templateName);
			main.getGroup().addImports(imports);
			Files.createDirectories(targetPath.getParent());
			String evaluate = main.evaluate();
			Files.write(targetPath, evaluate.getBytes());
		}
	}

	private List<TemplateDefinition> loadImports() {
		return imports.stream()
			.flatMap(name -> loader.loadGroup(name).getDefinitions().stream())
			.collect(toList());
	}
}
