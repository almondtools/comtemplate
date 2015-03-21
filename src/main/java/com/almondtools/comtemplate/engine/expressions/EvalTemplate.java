package com.almondtools.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateExpression;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;
import com.almondtools.comtemplate.engine.TemplateVariable;

public class EvalTemplate implements TemplateExpression {

	private String template;
	private TemplateDefinition definition;
	private List<TemplateVariable> arguments;

	public EvalTemplate(String template, TemplateDefinition definition, List<TemplateVariable> arguments) {
		this.template = template;
		this.definition = definition;
		this.arguments = arguments;
	}

	public EvalTemplate(String template, TemplateDefinition definition, TemplateVariable... arguments) {
		this(template, definition, asList(arguments));
	}

	public String getTemplate() {
		return template;
	}

	public TemplateDefinition getDefinition() {
		return definition;
	}

	public List<TemplateVariable> getArguments() {
		return arguments;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitEvalTemplate(this, scope);
	}

	@Override
	public String toString() {
		return '@' + template + arguments.stream()
			.map(argument -> argument.toString())
			.collect(joining(",", "(", ")"));
	}
}
