package com.almondtools.comtemplate.engine.expressions;


public class TestError extends ErrorExpression {

	private String msg;

	public TestError(String msg) {
		this.msg = msg;
	}

	public TestError() {
		this("test");
	}
	
	public String getMessage() {
		return msg;
	}
}
