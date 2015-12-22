package com.almondtools.comtemplate.parser;

import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateLoader;

public class UnsupportedLoader implements TemplateLoader {

	@Override
	public TemplateDefinition loadDefinition(String name) {
		throw new UnsupportedOperationException("cannot load definitions, use a valid loader");
	}

	@Override
	public TemplateGroup loadGroup(String name) {
		throw new UnsupportedOperationException("cannot load group, use a valid loader");
	}

}
