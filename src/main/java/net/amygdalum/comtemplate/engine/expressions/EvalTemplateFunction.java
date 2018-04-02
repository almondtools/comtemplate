package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.stream.Collectors;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;

public class EvalTemplateFunction implements TemplateExpression {

	private String template;
	private TemplateDefinition definition;
	private List<TemplateExpression> arguments;

	public EvalTemplateFunction(String template, TemplateDefinition definition, List<TemplateExpression> arguments) {
		this.template = template;
		this.definition = definition;
		this.arguments = arguments;
	}

	public EvalTemplateFunction(String template, TemplateDefinition definition, TemplateExpression... arguments) {
		this(template, definition, asList(arguments));
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

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitEvalTemplateFunction(this, scope);
	}

	@Override
	public String toString() {
		return '@' + template + arguments.stream()
			.map(argument -> argument.toString())
			.collect(Collectors.joining(",", "(", ")"));
	}

}
