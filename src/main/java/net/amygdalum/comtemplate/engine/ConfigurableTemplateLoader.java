package net.amygdalum.comtemplate.engine;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConfigurableTemplateLoader extends AbstractTemplateLoader implements TemplateLoader {

	private Path source;
	private boolean useClassPath;
	private List<Path> paths;

	public ConfigurableTemplateLoader() {
		this.paths = new ArrayList<>();
	}

	public ConfigurableTemplateLoader(TemplateCompiler compiler) {
		super(compiler);
		this.paths = new ArrayList<>();
	}

	public ConfigurableTemplateLoader withSource(Path source) {
		this.source = source;
		return this;
	}

	public ConfigurableTemplateLoader withClasspath(boolean useClassPath) {
		this.useClassPath = useClassPath;
		return this;
	}

	public ConfigurableTemplateLoader forPaths(String... pathNames) {
		List<Path> paths = Stream.of(pathNames)
			.map(path -> Paths.get(path))
			.collect(toList());
		return forPaths(paths);
	}

	public ConfigurableTemplateLoader forPaths(List<Path> paths) {
		this.paths = paths;
		return this;
	}

	protected ClassLoader getClassLoader() {
		return getClass().getClassLoader();
	}

	@Override
	public InputStream loadSource(String name) throws IOException {
		String file = pathOf(name);
		if (source != null) {
			Path sourcePath = source.resolve(file);
			if (Files.exists(sourcePath)) {
				return Files.newInputStream(sourcePath);
			}
		}
		if (useClassPath) {
			InputStream stream = getClassLoader().getResourceAsStream(file);
			if (stream != null) {
				return stream;
			}
		}
		for (Path path : paths) {
			Path filePath = path.resolve(pathOf(name));
			if (Files.exists(filePath)) {
				return Files.newInputStream(filePath);
			}
		}
		throw new NoSuchFileException(name, null, "cannot find files in configured paths: " + paths.stream()
			.map(path -> path.toString())
			.collect(joining("\n\t", "\n\t", "")));
	}

	@Override
	public String resolveResource(String name) {
		String file = pathOf(name);
		if (source != null) {
			Path sourcePath = source.resolve(file);
			if (Files.exists(sourcePath)) {
				return sourcePath.toString();
			}
		}
		if (useClassPath) {
			URL url = getClassLoader().getResource(file);
			if (url != null) {
				return url.toString();
			}
		}
		for (Path path : paths) {
			Path filePath = path.resolve(pathOf(name));
			if (Files.exists(filePath)) {
				return filePath.toString();
			}
		}
		return "";
	}

}
