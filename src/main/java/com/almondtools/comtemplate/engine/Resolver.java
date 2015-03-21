package com.almondtools.comtemplate.engine;

import java.util.List;

import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;

public interface Resolver {

	public static final Resolver NULL = new Resolver() {

		@Override
		public TemplateImmediateExpression resolve(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
			return new ExpressionResolutionError(base, function, arguments, scope, null);
		}
	};

	TemplateImmediateExpression resolve(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope);

}
