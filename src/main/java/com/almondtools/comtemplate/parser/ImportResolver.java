package com.almondtools.comtemplate.parser;

import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateGroup;

public interface ImportResolver {

	TemplateDefinition importDefinition(String name);

	TemplateGroup importGroup(String name);

}
