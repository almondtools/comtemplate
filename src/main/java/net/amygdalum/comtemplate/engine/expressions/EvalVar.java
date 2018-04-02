package net.amygdalum.comtemplate.engine.expressions;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;

public class EvalVar implements TemplateExpression {

	private String name;
	private TemplateDefinition definition;

	public EvalVar(String name, TemplateDefinition definition) {
		this.name = name;
		this.definition = definition;
	}

	public String getName() {
		return name;
	}

	public TemplateDefinition getDefinition() {
		return definition;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitEvalVar(this, scope);
	}

	@Override
	public String toString() {
		return "@" + name;
	}
}
