package com.almondtools.comtemplate.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTemplateLoader implements TemplateLoader {

	private TemplateCompiler compiler;
	private Map<String, TemplateGroup> resolvedGroups;


	public AbstractTemplateLoader(TemplateCompiler compiler) {
		this.compiler = compiler;
		this.resolvedGroups = new HashMap<String, TemplateGroup>();
	}
	
	public AbstractTemplateLoader() {
		this(new DefaultTemplateCompiler());
	}
	
	public TemplateGroup compile(String name, InputStream stream) throws IOException {
		return compiler.compile(name, stream, this);
	}

	@Override
	public TemplateGroup loadGroup(String name) {
		TemplateGroup resolved = resolvedGroups.get(name);
		if (resolved == null) {
			try {
				InputStream stream = loadSource(name);
				resolved = compile(name, stream);
				resolvedGroups.put(name, resolved);
			} catch (IOException e) {
				throw new TemplateGroupNotFoundException(name);
			}
		}
		return resolved;
	}

	public abstract InputStream loadSource(String name);

	@Override
	public TemplateDefinition loadDefinition(String name) {
		int separator = name.lastIndexOf('.');
		String file = name.substring(0, separator);
		String definition = name.substring(separator + 1);
		TemplateGroup group = loadGroup(file);
		return group.getDefinition(definition);
	}

}
