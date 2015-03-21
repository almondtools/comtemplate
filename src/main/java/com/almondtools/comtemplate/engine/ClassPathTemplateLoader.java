package com.almondtools.comtemplate.engine;

import static com.almondtools.picklock.ObjectAccess.unlock;
import static java.nio.file.Files.isDirectory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.almondtools.picklock.PicklockException;

public class ClassPathTemplateLoader extends AbstractTemplateLoader implements TemplateLoader {

	private Map<String, TemplateGroup> resolvedGroups;

	public ClassPathTemplateLoader() {
		this.resolvedGroups = new HashMap<String, TemplateGroup>();
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
	public TemplateGroup loadGroup(String name) {
		TemplateGroup resolved = resolvedGroups.get(name);
		if (resolved == null) {
			try {
				InputStream stream = getClassLoader().getResourceAsStream(pathOf(name) + ".ctp");
				resolved = compile(name, stream);
				resolvedGroups.put(name, resolved);
			} catch (IOException e) {
				throw new TemplateGroupNotFoundException(name);
			}
		}
		return resolved;
	}

	private String pathOf(String name) {
		return name.replace('.', '/');
	}

	interface ClassPathUrls {
		void addURL(URL url);
	}
}
