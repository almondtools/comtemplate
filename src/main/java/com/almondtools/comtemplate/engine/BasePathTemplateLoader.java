package com.almondtools.comtemplate.engine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BasePathTemplateLoader extends AbstractTemplateLoader implements TemplateLoader {

	private Path base;

	public BasePathTemplateLoader(String base) {
		this.base = Paths.get(base);
	}

	public BasePathTemplateLoader(TemplateCompiler compiler,String base) {
		super(compiler);
		this.base = Paths.get(base);
	}

	@Override
	public InputStream loadSource(String name) {
		try {
			return Files.newInputStream(base.resolve(pathOf(name)));
		} catch (IOException e) {
			return null;
		}
	}

}
