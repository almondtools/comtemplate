package com.almondtools.comtemplate.engine.resolvers;

import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;

public abstract class ExclusiveTypeFunctionResolver<T> extends FunctionResolver {

	private Class<T> clazz;

	public ExclusiveTypeFunctionResolver(Class<T> clazz, String name, int arity) {
		super(name, arity);
		this.clazz = clazz;
	}

	public ExclusiveTypeFunctionResolver(Class<T> clazz, String name) {
		super(name);
		this.clazz = clazz;
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (clazz.isInstance(base)) {
			return resolveTyped(clazz.cast(base), arguments, scope);
		}
		return new ExpressionResolutionError(base, getName(), arguments, scope, this);
	}

	public abstract TemplateImmediateExpression resolveTyped(T base, List<TemplateImmediateExpression> arguments, Scope scope);
}
