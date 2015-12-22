package com.almondtools.comtemplate.engine;

public interface TemplateLoader {

	TemplateGroup loadGroup(String name);

	TemplateDefinition loadDefinition(String name);

}
