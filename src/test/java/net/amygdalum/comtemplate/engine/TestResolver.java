package net.amygdalum.comtemplate.engine;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;

import java.util.List;

public class TestResolver implements Resolver {

	private Class<? extends TemplateImmediateExpression> clazz;

	public TestResolver(Class<? extends TemplateImmediateExpression> clazz) {
		this.clazz = clazz;
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
		return string(base.toString() + "." + function + arguments.stream()
			.map(arg -> arg.toString())
			.collect(joining(",","(",")")));
	}

	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(clazz);
	}
}
