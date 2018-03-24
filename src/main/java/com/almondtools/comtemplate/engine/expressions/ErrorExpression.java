package com.almondtools.comtemplate.engine.expressions;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;

public abstract class ErrorExpression implements TemplateImmediateExpression {

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitErrorExpression(this, scope);
	}

	@Override
	public <T> T as(Class<T> clazz) {
		return null;
	}

	@Override
	public String getText() {
		return "";
	}

	public abstract String getMessage();

	protected String getScopeStack(Scope scope) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("scope stack:");
		while (scope != null) {
			buffer.append("\n\t" + scope.getDefinition().getName() + ":" + scope.getVariables().toString());
			scope = scope.getParent();
		}
		return buffer.toString();
	}

	@Override
	public String toString() {
		return "<" + getClass().getSimpleName().toLowerCase() + ">";
	}
}
