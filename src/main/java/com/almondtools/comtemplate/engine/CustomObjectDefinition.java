package com.almondtools.comtemplate.engine;

import java.util.List;

import com.almondtools.comtemplate.engine.expressions.ToObject;

public class CustomObjectDefinition extends TemplateDefinition {

	private TemplateExpression result;

	public CustomObjectDefinition(String name, List<TemplateParameter> parameters) {
		super(name, parameters);
	}

	public CustomObjectDefinition(String name, Object... parameters) {
		super(name, parameters);
	}

	public void setResult(TemplateExpression result) {
		this.result = new ToObject(getName(), result);
	}

	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		List<TemplateVariable> variables = createVariables(arguments);
		Scope scope = new Scope(parent, this, variables);
		return result.apply(interpreter, scope);
	}

}
