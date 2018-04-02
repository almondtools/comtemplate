package net.amygdalum.comtemplate.engine.expressions;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;

public class EvalAttribute implements TemplateExpression {

	private TemplateExpression base;
	private String attribute;

	public EvalAttribute(TemplateExpression base, String attribute) {
		this.base = base;
		this.attribute = attribute;
	}

	public TemplateExpression getBase() {
		return base;
	}

	public String getAttribute() {
		return attribute;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitEvalAttribute(this, scope);
	}
	
	@Override
	public String toString() {
		return base.toString() + '.' + attribute;
	}

}
