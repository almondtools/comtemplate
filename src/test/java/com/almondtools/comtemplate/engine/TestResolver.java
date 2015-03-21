package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.stream.Collectors.joining;

import java.util.List;

public class TestResolver implements Resolver {

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
		return string(base.toString() + "." + function + arguments.stream()
			.map(arg -> arg.toString())
			.collect(joining(",","(",")")));
	}

}
