package net.amygdalum.comtemplate.engine;

import static java.util.stream.Collectors.joining;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TestTemplateDefinition extends TemplateDefinition {

	public TestTemplateDefinition(String name, List<TemplateParameter> parameters) {
		this(name, parameters, new TemplateGroup(name, "testresource"));
	}

	public TestTemplateDefinition(String name, Object... parameters) {
		this(name, TemplateParameter.toParams(parameters), new TemplateGroup(name, "testresource"));
	}

	public TestTemplateDefinition(String name, List<TemplateParameter> parameters, TemplateGroup group) {
		super(name, parameters);
		setGroup(group);
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