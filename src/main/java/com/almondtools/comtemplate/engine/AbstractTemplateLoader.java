package com.almondtools.comtemplate.engine;

import static java.util.stream.Collectors.toList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
		return compiler.compileLibrary(name, stream, this);
	}

	public TemplateDefinition compileMain(String name, InputStream stream) throws IOException {
		return compiler.compileMain(name, stream, this);
	}

	public TemplateDefinition compileText(String text) throws IOException {
		return compileMain("", new ByteArrayInputStream(text.getBytes()));
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
	public TemplateDefinition loadMain(String name) {
		try {
			InputStream stream = loadSource(name);
			TemplateDefinition main = compileMain(name, stream);
			InputStream groupStream = loadSource("_group");
			if (groupStream != null) {
				TemplateGroup group = compile("", groupStream);
				List<TemplateDefinition> imports = Stream.concat(
					group.getDefinitions().stream(), 
					group.getImports().stream())
					.collect(toList());
				main.getGroup().addImports(imports);
			}
			return main;
		} catch (IOException e) {
			throw new TemplateGroupNotFoundException(name);
		}
	}

	@Override
	public TemplateDefinition loadDefinition(String name) {
		int separator = name.lastIndexOf('.');
		if (separator < 0) {
			throw new TemplateDefinitionNotFoundException("unknown", name);
		}
		String file = name.substring(0, separator);
		String definition = name.substring(separator + 1);
		TemplateGroup group = loadGroup(file);
		return group.getDefinition(definition);
	}

	protected String pathOf(String name) {
		return name.replace('.', '/')  + ".ctp";
	}

}
