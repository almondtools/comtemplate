package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;

import java.util.List;

import com.almondtools.comtemplate.engine.expressions.ToObject;

public class ValueDefinition extends TemplateDefinition {

	private TemplateExpression value;

	public ValueDefinition(String name, List<TemplateParameter> parameters) {
		super(name, parameters);
	}

	public ValueDefinition(String name, Object... parameters) {
		super(name, parameters);
	}

	public void setValue(TemplateExpression value) {
		this.value = value;
	}
	
	public void setObjectValue(TemplateExpression value) {
		this.value = new ToObject(getName(), value);
	}

	public TemplateExpression getValue() {
		return value;
	}
	
	public TemplateVariable toVariable() {
		return var(getName(), value);
	}

	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		List<TemplateVariable> variables = createVariables(arguments);
		Scope scope = new Scope(parent, this, variables);
		return value.apply(interpreter, scope);
	}

}
