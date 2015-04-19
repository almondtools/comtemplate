package com.almondtools.comtemplate.engine.resolvers;

import java.util.List;

import com.almondtools.comtemplate.engine.Resolver;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;

public abstract class ExclusiveTypeResolver<T> implements Resolver {

	private Class<T> clazz;

	public ExclusiveTypeResolver(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (clazz.isInstance(base)) {
			return resolveTyped(clazz.cast(base), function, arguments, scope);
		}
		return new ExpressionResolutionError(base, function, arguments, scope, this);
	}

	public abstract TemplateImmediateExpression resolveTyped(T base, String function, List<TemplateImmediateExpression> arguments, Scope scope);
}
