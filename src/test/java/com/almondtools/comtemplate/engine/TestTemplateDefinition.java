package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.stream.Collectors.joining;

import java.util.List;

public class TestTemplateDefinition extends TemplateDefinition {

	public TestTemplateDefinition(String name, List<TemplateParameter> parameters) {
		super(name, parameters);
	}

	public TestTemplateDefinition(String name, Object... parameters) {
		super(name, parameters);
	}

	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		return string("test: " + arguments.stream()
			.map(TemplateVariable::toString)
			.collect(joining(",")));
	}

}