package com.almondtools.comtemplate.engine.expressions;


public class IOResolutionError extends ErrorExpression {

	private String message;

	public IOResolutionError(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "expression resolution failed with: " + message;
	}

}
