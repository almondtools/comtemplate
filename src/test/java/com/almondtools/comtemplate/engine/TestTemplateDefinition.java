package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TestTemplateDefinition extends TemplateDefinition {

	public TestTemplateDefinition(String name, List<TemplateParameter> parameters) {
		super(name, parameters);
	}

	public TestTemplateDefinition(String name, Object... parameters) {
		super(name, parameters);
	}

	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		if (arguments.isEmpty()) {
			return string("test: " + Optional.ofNullable(parent)
				.map(mapper -> mapper.getVariables())
				.map(var -> var.stream())
				.orElse(Stream.empty())
				.map(TemplateVariable::toString)
				.collect(joining(",")));
		} else {
			return string("test: " + arguments.stream()
				.map(TemplateVariable::toString)
				.collect(joining(",")));
		}
	}

}