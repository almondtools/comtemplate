package com.almondtools.comtemplate.engine.resolvers;

import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;

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
