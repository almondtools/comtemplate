package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.stream.Stream;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.TemplateVariable;

public class EvalTemplateMixed implements TemplateExpression {

	private String template;
	private TemplateDefinition definition;
	private List<TemplateExpression> arguments;
	private List<TemplateVariable> namedArguments;

	public EvalTemplateMixed(String template, TemplateDefinition definition, List<TemplateExpression> arguments, List<TemplateVariable> namedArguments) {
		this.template = template;
		this.definition = definition;
		this.arguments = arguments;
		this.namedArguments = namedArguments;
	}

	public EvalTemplateMixed(String template, TemplateDefinition definition, TemplateExpression[] arguments, TemplateVariable... namedArguments) {
		this(template, definition, asList(arguments), asList(namedArguments));
	}

	public String getTemplate() {
		return template;
	}

	public TemplateDefinition getDefinition() {
		return definition;
	}

	public List<TemplateExpression> getArguments() {
		return arguments;
	}

	public List<TemplateVariable> getNamedArguments() {
		return namedArguments;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitEvalTemplateMixed(this, scope);
	}

	@Override
	public String toString() {
		return '@' + template + Stream.concat(
			arguments.stream()
				.map(argument -> argument.toString()),
			namedArguments.stream()
				.map(argument -> argument.toString()))
			.collect(joining(",", "(", ")"));
	}
}
