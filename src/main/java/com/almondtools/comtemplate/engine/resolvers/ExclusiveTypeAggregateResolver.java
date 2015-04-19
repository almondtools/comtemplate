package com.almondtools.comtemplate.engine.resolvers;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;


public abstract class ExclusiveTypeAggregateResolver<T extends TemplateImmediateExpression> extends ExclusiveTypeResolver<T>{
	
	private Map<String, ExclusiveTypeFunctionResolver<T>> functions;

	@SafeVarargs
	public ExclusiveTypeAggregateResolver(Class<T> clazz, ExclusiveTypeFunctionResolver<T>... functions) {
		super(clazz);
		this.functions = functionMapOf(functions);
	}

	@SafeVarargs
	public static <T> Map<String, ExclusiveTypeFunctionResolver<T>> functionMapOf(ExclusiveTypeFunctionResolver<T>... functions) {
		return Stream.of(functions)
				.collect(toMap(f -> f.getName(), f -> f));
	}

	@Override
	public TemplateImmediateExpression resolveTyped(T base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
		ExclusiveTypeFunctionResolver<T> resolver = functions.get(function);
		if (resolver == null) {
			return new ExpressionResolutionError(base, function, arguments, scope, this);
		} else {
			return resolver.resolve(base, arguments, scope);
		}
	}

}
