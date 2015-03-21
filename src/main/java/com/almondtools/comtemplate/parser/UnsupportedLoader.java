package com.almondtools.comtemplate.parser;

import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateLoader;

public class UnsupportedLoader implements TemplateLoader {

	@Override
	public TemplateDefinition loadDefinition(String name) {
		throw new UnsupportedOperationException("cannot import definitions, use a valid import resolver");
	}

	@Override
	public TemplateGroup loadGroup(String name) {
		throw new UnsupportedOperationException("cannot import group, use a valid import resolver");
	}

}
