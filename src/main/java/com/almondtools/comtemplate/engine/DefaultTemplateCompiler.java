package com.almondtools.comtemplate.engine;

import java.io.IOException;
import java.io.InputStream;

import com.almondtools.comtemplate.parser.TemplateGroupBuilder;

public class DefaultTemplateCompiler implements TemplateCompiler {

	public TemplateGroup compileLibrary(String name, InputStream stream, TemplateLoader loader) throws IOException {
		if (stream == null) {
			throw new TemplateGroupNotFoundException(name);
		}
		return TemplateGroupBuilder.library(name, stream, loader).buildGroup();
	}

	@Override
	public TemplateDefinition compileMain(String name, InputStream stream, TemplateLoader loader) throws IOException {
		if (stream == null) {
			throw new TemplateGroupNotFoundException(name);
		}
		return TemplateGroupBuilder.main(name, stream, loader).buildMain();
	}
}
