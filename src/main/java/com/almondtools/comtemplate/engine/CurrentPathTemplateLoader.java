package com.almondtools.comtemplate.engine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CurrentPathTemplateLoader extends AbstractTemplateLoader implements TemplateLoader {

	public CurrentPathTemplateLoader() {
	}

	public CurrentPathTemplateLoader(TemplateCompiler compiler) {
		super(compiler);
	}

	@Override
	public InputStream loadSource(String name) {
		try {
			return Files.newInputStream(Paths.get(pathOf(name) + ".ctp"));
		} catch (IOException e) {
			return null;
		}
	}

}
