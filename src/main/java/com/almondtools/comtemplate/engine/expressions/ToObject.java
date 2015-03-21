package com.almondtools.comtemplate.engine.expressions;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpression;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class ToObject implements TemplateExpression {

	public static final String TYPE = "_type";
	public static final String SUPERTYPE = "_supertypes";
	public static final String VALUE = "_value";

	private String type;
	private TemplateExpression expression;

	public ToObject(String type, TemplateExpression expression) {
		this.type = type;
		this.expression = expression;
	}

	public String getType() {
		return type;
	}

	public TemplateExpression getExpression() {
		return expression;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitToObject(this, scope);
	}

	@Override
	public String toString() {
		return type + '(' + expression.toString() + ')';
	}
}
