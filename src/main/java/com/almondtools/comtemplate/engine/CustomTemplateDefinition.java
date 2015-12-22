package com.almondtools.comtemplate.engine;

import java.util.ArrayList;
import java.util.List;

import com.almondtools.comtemplate.engine.expressions.Evaluated;

public class CustomTemplateDefinition extends TemplateDefinition {

	private List<TemplateExpression> expressions;

	public CustomTemplateDefinition(String name, List<TemplateParameter> parameters) {
		super(name, parameters);
		this.expressions = new ArrayList<>();
	}

	public CustomTemplateDefinition(String name, Object... parameters) {
		super(name, parameters);
		this.expressions = new ArrayList<>();
	}

	public void add(TemplateExpression expression) {
		expressions.add(expression);
	}

	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		List<TemplateVariable> variables = createVariables(arguments);
		Scope scope = new Scope(parent, this, variables);
		return expressions.stream()
			.map(expression -> expression.apply(interpreter, scope))
			.collect(Evaluated.assembling());
	}


}
