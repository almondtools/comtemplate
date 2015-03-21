package com.almondtools.comtemplate.engine.expressions;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpression;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class Defaulted implements TemplateExpression {

	private TemplateExpression expression;
	private TemplateExpression defaultExpression;

	public Defaulted(TemplateExpression expression, TemplateExpression defaultExpression) {
		this.expression = expression;
		this.defaultExpression = defaultExpression;
	}

	public TemplateExpression getExpression() {
		return expression;
	}
	
	public TemplateExpression getDefaultExpression() {
		return defaultExpression;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitDefaulted(this, scope);
	}

	@Override
	public String toString() {
		return expression.toString() + "?:" + defaultExpression.toString();
	}
}
