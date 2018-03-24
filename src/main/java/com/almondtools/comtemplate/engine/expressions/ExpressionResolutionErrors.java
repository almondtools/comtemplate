package com.almondtools.comtemplate.engine.expressions;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import com.almondtools.comtemplate.engine.Resolver;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;

public class ExpressionResolutionErrors extends ExpressionResolutionError {

	private List<ErrorExpression> errors;

	public ExpressionResolutionErrors(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope, Resolver resolver, List<ErrorExpression> errors) {
		super(base, function, arguments, scope, resolver);
		this.errors = errors.stream()
			.flatMap(ExpressionResolutionErrors::flatten)
			.collect(toList());
	}
	
	private static Stream<ErrorExpression> flatten(ErrorExpression errorExpression) {
		if (errorExpression instanceof ExpressionResolutionErrors) {
			return ((ExpressionResolutionErrors) errorExpression).errors.stream();
		} else {
			return Stream.of(errorExpression);
		}
	}
	
	public List<ErrorExpression> getErrors() {
		return errors;
	}

}
