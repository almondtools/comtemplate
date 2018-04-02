package net.amygdalum.comtemplate.parser;

import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateGroup;

public interface ImportResolver {

	TemplateDefinition importDefinition(String name);

	TemplateGroup importGroup(String name);

}
