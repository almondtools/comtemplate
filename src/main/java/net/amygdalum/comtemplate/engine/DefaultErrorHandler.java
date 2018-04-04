package net.amygdalum.comtemplate.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import net.amygdalum.comtemplate.engine.expressions.ErrorExpression;

public class DefaultErrorHandler implements ErrorHandler {

	private List<Default> defaults;

	public DefaultErrorHandler() {
		this.defaults = new ArrayList<>();
	}

	@Override
	public TemplateImmediateExpression handle(ErrorExpression errorExpression) {
		Messages.error(errorExpression.getMessage());
		return errorExpression;
	}

	@Override
	public TemplateImmediateExpression clear(TemplateExpression expression, TemplateImmediateExpression resolvedExpression) {
		if (resolvedExpression instanceof ErrorExpression) {
			resolvedExpression = applyDefaults(expression).orElse(resolvedExpression);
		}
		if (resolvedExpression instanceof ErrorExpression) {
			return handle((ErrorExpression) resolvedExpression);
		}
		return resolvedExpression;
	}

	private Optional<TemplateImmediateExpression> applyDefaults(TemplateExpression expression) {
		return defaults.stream()
			.filter(value -> value.expression == expression)
			.findFirst()
			.map(value -> value.defaultExpression.get());
	}

	@Override
	public void addDefault(TemplateExpression expression, Supplier<TemplateImmediateExpression> defaultExpression) {
		defaults.add(new Default(expression, defaultExpression));
	}

	private static class Default {
		public TemplateExpression expression;
		public Supplier<TemplateImmediateExpression> defaultExpression;

		public Default(TemplateExpression expression, Supplier<TemplateImmediateExpression> defaultExpression) {
			this.expression = expression;
			this.defaultExpression = defaultExpression;
		}

	}
}
