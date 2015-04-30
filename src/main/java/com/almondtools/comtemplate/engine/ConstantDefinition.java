package com.almondtools.comtemplate.engine;

import static java.util.Collections.emptyList;

import java.util.List;

public class ConstantDefinition extends TemplateDefinition {

	private TemplateExpression value;

	public ConstantDefinition(String name) {
		super(name, emptyList());
	}

	public void setValue(TemplateExpression value) {
		this.value = value;
	}
	
	public TemplateExpression getValue() {
		return value;
	}
	
	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		List<TemplateVariable> variables = createVariables(arguments);
		Scope scope = new Scope(parent, this, variables);
		return value.apply(interpreter, scope);
	}

	public TemplateVariable toVariable() {
		return TemplateVariable.var(getName(), value);
	}

}
