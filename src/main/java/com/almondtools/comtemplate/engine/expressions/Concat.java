package com.almondtools.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpression;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class Concat implements TemplateExpression {

	private List<TemplateExpression> expressions;

	public Concat(List<TemplateExpression> expressions) {
		this.expressions = expressions;
	}

	public Concat(TemplateExpression... expressions) {
		this(asList(expressions));
	}

	public List<TemplateExpression> getExpressions() {
		return expressions;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitConcat(this, scope);
	}

	@Override
	public String toString() {
		return expressions.stream()
			.map(expression -> expression.toString())
			.collect(joining("~"));
	}
}
