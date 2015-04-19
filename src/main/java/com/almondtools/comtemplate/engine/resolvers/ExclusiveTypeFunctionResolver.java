package com.almondtools.comtemplate.engine.resolvers;

import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;

public abstract class ExclusiveTypeFunctionResolver<T extends TemplateImmediateExpression> extends ExclusiveTypeResolver<T> {

	private String name;
	private int arity;

	public ExclusiveTypeFunctionResolver(Class<T> clazz, String name, int arity) {
		super(clazz);
		this.name = name;
		this.arity = arity;
	}

	public ExclusiveTypeFunctionResolver(Class<T> clazz, String name) {
		this(clazz, name, 0);
	}
	
	public String getName() {
		return name;
	}
	
	public int getArity() {
		return arity;
	}
	
	@Override
	public TemplateImmediateExpression resolveTyped(T base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (name.equals(function) && arguments.size() == arity) {
			return resolveTyped(base, arguments, scope);
		}
		return new ExpressionResolutionError(base, name, arguments, scope, this);
	}

	public abstract TemplateImmediateExpression resolveTyped(T base, List<TemplateImmediateExpression> arguments, Scope scope);
}
