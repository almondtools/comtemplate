package com.almondtools.comtemplate.engine;

import java.io.IOException;
import java.io.InputStream;

import com.almondtools.comtemplate.parser.TemplateGroupBuilder;

public class DefaultTemplateCompiler implements TemplateCompiler {

	public TemplateGroup compileLibrary(String name, String resource, InputStream stream, TemplateLoader loader) throws IOException {
		if (stream == null) {
			throw new TemplateGroupNotFoundException(name);
		}
		return TemplateGroupBuilder.library(name, resource, stream, loader).buildGroup();
	}

	@Override
	public TemplateDefinition compileMain(String name, String resource, InputStream stream, TemplateLoader loader) throws IOException {
		if (stream == null) {
			throw new TemplateGroupNotFoundException(name);
		}
		return TemplateGroupBuilder.main(name, resource, stream, loader).buildMain();
	}
}
