package com.almondtools.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;

import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.BooleanLiteral;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;

public class NotResolver extends FunctionResolver {

	public NotResolver() {
		super("not");
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (base instanceof BooleanLiteral) {
			return ((BooleanLiteral) base).invert();
		} else {
			return new ExpressionResolutionError(base, "not", arguments, scope, this);
		}
	}
	
	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(BooleanLiteral.class);
	}
	
}
