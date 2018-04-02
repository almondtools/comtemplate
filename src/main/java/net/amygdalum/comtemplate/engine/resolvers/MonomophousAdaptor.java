package net.amygdalum.comtemplate.engine.resolvers;

import java.util.List;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ResolvedMapLiteral;

public abstract class MonomophousAdaptor {

	private String type;

	public MonomophousAdaptor(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public abstract TemplateImmediateExpression resolve(ResolvedMapLiteral base, List<TemplateImmediateExpression> arguments, Scope scope);

}
