package com.almondtools.comtemplate.engine.expressions;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpression;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class Cast implements TemplateExpression {

	private TemplateExpression expression;
	private String type;

	public Cast(TemplateExpression expression, String type) {
		this.expression = expression;
		this.type = type;
	}

	public TemplateExpression getExpression() {
		return expression;
	}
	
	public String getType() {
		return type;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitCast(this, scope);
	}

	@Override
	public String toString() {
		return '(' + type + ") " + expression.toString();
	}

}
