package com.almondtools.comtemplate.engine;

import com.almondtools.comtemplate.engine.expressions.ErrorExpression;

public interface ErrorHandler {

	TemplateImmediateExpression handle(ErrorExpression errorExpression);

}
