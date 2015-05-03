package com.almondtools.comtemplate.engine;

import java.io.IOException;
import java.io.InputStream;

import com.almondtools.comtemplate.parser.TemplateGroupBuilder;

public class DefaultTemplateCompiler implements TemplateCompiler {

	public TemplateGroup compile(String name, InputStream stream, TemplateLoader loader) throws IOException {
		if (stream == null) {
			throw new TemplateGroupNotFoundException(name);
		}
		return new TemplateGroupBuilder(name, stream, loader).build();
	}

}
