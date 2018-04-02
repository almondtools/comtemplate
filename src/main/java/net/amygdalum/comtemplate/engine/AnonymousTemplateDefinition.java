package net.amygdalum.comtemplate.engine;

import java.util.List;

public class AnonymousTemplateDefinition extends TemplateDefinition {

	public AnonymousTemplateDefinition(TemplateGroup group) {
		super("anonymous");
		setGroup(group);
	}

	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		return null;
	}

}
