package com.almondtools.comtemplate.engine;

import static com.almondtools.picklock.ObjectAccess.unlock;
import static java.nio.file.Files.isDirectory;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.almondtools.picklock.PicklockException;

public class ClassPathTemplateLoader extends AbstractTemplateLoader implements TemplateLoader {

	public ClassPathTemplateLoader() {
	}

	public ClassPathTemplateLoader(TemplateCompiler compiler) {
		super(compiler);
	}

	public ClassPathTemplateLoader addClassPath(String pathName) {
		try {
			Path path = Paths.get(pathName);
			ClassPathUrls classPathUrls = unlock(getClassLoader()).features(ClassPathUrls.class);
			Path absolute = path.toAbsolutePath().normalize();
			if (isDirectory(absolute)) {
				classPathUrls.addURL(absolute.toUri().toURL());
				return this;
			} else {
				throw new ClassPathResolutionException(pathName);
			}
		} catch (MalformedURLException | PicklockException e) {
			throw new ClassPathResolutionException(pathName);
		}
	}

	protected ClassLoader getClassLoader() {
		return ClassPathTemplateLoader.class.getClassLoader();
	}

	@Override
	public InputStream loadSource(String name) {
		return getClassLoader().getResourceAsStream(pathOf(name) + ".ctp");
	}

	interface ClassPathUrls {
		void addURL(URL url);
	}
}
