package com.almondtools.comtemplate.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almondtools.comtemplate.engine.expressions.ErrorExpression;

public class DefaultErrorHandler implements ErrorHandler {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultErrorHandler.class);

	@Override
	public TemplateImmediateExpression handle(ErrorExpression errorExpression) {
		LOG.error(errorExpression.getMessage());
		return errorExpression;
	}

}
