package com.almondtools.comtemplate.engine.expressions;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpression;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class IgnoreErrors implements TemplateExpression {

	private TemplateExpression expression;

	public IgnoreErrors(TemplateExpression expression) {
		this.expression = expression;
	}

	public TemplateExpression getExpression() {
		return expression;
	}
	
	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitIgnoreErrors(this, scope);
	}

	@Override
	public String toString() {
		return expression.toString() + '!';
	}
}