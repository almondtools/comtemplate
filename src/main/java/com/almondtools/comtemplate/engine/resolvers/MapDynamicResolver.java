package com.almondtools.comtemplate.engine.resolvers;

import java.util.List;
import java.util.Optional;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;

public class MapDynamicResolver extends AbstractResolver<ResolvedMapLiteral> {

	public MapDynamicResolver() {
		super(ResolvedMapLiteral.class);
	}

	@Override
	public TemplateImmediateExpression resolveTyped(ResolvedMapLiteral base, String attribute, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (arguments.isEmpty()) {
			return Optional.ofNullable(base.getAttribute(attribute))
				.orElseGet(() ->  new ExpressionResolutionError(base, attribute, arguments, scope, this));
		}
		return new ExpressionResolutionError(base, attribute, arguments, scope, this);
	}

}
