package com.almondtools.comtemplate.engine;

public interface TemplateGroupLoader {

	TemplateGroup loadGroup(String name);

	TemplateDefinition loadDefinition(String name);

}
