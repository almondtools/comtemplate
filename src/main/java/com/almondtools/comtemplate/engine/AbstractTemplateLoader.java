package com.almondtools.comtemplate.engine;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractTemplateLoader implements TemplateLoader {

	private TemplateCompiler compiler;

	public AbstractTemplateLoader(TemplateCompiler compiler) {
		this.compiler = compiler;
	}
	
	public AbstractTemplateLoader() {
		this(new DefaultTemplateCompiler());
	}
	
	public TemplateGroup compile(String name, InputStream stream) throws IOException {
		return compiler.compile(name, stream, this);
	}

	@Override
	public abstract TemplateGroup loadGroup(String name);
	
	@Override
	public TemplateDefinition loadDefinition(String name) {
		int separator = name.lastIndexOf('.');
		String file = name.substring(0, separator);
		String definition = name.substring(separator + 1);
		TemplateGroup group = loadGroup(file);
		return group.getDefinition(definition);
	}

}
