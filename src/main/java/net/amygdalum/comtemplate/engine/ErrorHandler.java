package net.amygdalum.comtemplate.engine;

import java.util.function.Supplier;

import net.amygdalum.comtemplate.engine.expressions.ErrorExpression;

public interface ErrorHandler {

	TemplateImmediateExpression handle(ErrorExpression errorExpression);

	TemplateImmediateExpression clear(TemplateExpression expression, TemplateImmediateExpression resoltvedExpression);

	void addDefault(TemplateExpression expression, Supplier<TemplateImmediateExpression> defaultExpression);

}
