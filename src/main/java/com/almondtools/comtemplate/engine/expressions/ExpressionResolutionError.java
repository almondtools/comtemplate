package com.almondtools.comtemplate.engine.expressions;

import java.util.List;
import java.util.stream.Collectors;

import com.almondtools.comtemplate.engine.Resolver;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;

public class ExpressionResolutionError extends ErrorExpression {

	private TemplateImmediateExpression base;
	private String function;
	private List<TemplateImmediateExpression> arguments;
	private Scope scope;
	private Resolver resolver;

	public ExpressionResolutionError(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope, Resolver resolver) {
		this.base = base;
		this.function = function;
		this.arguments = arguments;
		this.scope = scope;
		this.resolver = resolver;
	}

	@Override
	public String getMessage() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("cannot evaluate <").append(base.toString()).append('.').append(function).append(arguments.stream()
			.map(argument -> argument.toString())
			.collect(Collectors.joining(",", "(", ")"))).append('>');
		if (resolver != null) {
			buffer.append("\nevaluated by <").append(resolver.getClass().getSimpleName()).append(">");
		}
		if (scope != null) {
			TemplateDefinition definition = scope.getDefinition();
			if (definition != null) {
				buffer.append("\naccessed in <").append(definition.getLocation()).append(">");
			}
			buffer.append('\n').append(getScopeStack(scope));
		}
		return buffer.toString();
	}

}
