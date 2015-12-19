package com.almondtools.comtemplate.engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CompositeTemplateLoader implements TemplateLoader {

	private TemplateCompiler compiler;
	private TemplateGroupLoader group;
	private TemplateMainLoader main;

	public CompositeTemplateLoader(TemplateCompiler compiler, TemplateGroupLoader group, TemplateMainLoader main) {
		this.compiler = compiler;
		this.group = group;
		this.main = main;
	}

	public CompositeTemplateLoader(TemplateLoader group, TemplateLoader main) {
		this(new DefaultTemplateCompiler(), group, main);
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
		return group.loadGroup(name);
	}

	@Override
	public TemplateDefinition loadMain(String name) {
		return main.loadMain(name);
	}

	@Override
	public TemplateDefinition loadDefinition(String name) {
		int separator = name.lastIndexOf('.');
		String file = name.substring(0, separator);
		String definition = name.substring(separator + 1);
		TemplateGroup group = loadGroup(file);
		return group.getDefinition(definition);
	}

	protected String pathOf(String name) {
		return name.replace('.', '/');
	}

}
