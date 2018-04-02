package net.amygdalum.comtemplate.parser;

import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateGroup;
import net.amygdalum.comtemplate.engine.TemplateLoader;

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
