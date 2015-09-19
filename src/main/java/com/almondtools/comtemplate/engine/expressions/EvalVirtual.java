package com.almondtools.comtemplate.engine.expressions;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpression;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class EvalVirtual implements TemplateExpression {

	private TemplateExpression base;
	private TemplateExpression attribute;

	public EvalVirtual(TemplateExpression base, TemplateExpression attribute) {
		this.base = base;
		this.attribute = attribute;
	}

	public TemplateExpression getBase() {
		return base;
	}

	public TemplateExpression getAttribute() {
		return attribute;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitEvalVirtual(this, scope);
	}
	
	@Override
	public String toString() {
		return base.toString() + ".(" + attribute + ')';
	}

}
