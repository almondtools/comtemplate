package com.almondtools.comtemplate.engine;

public class ClassPathResolutionException extends ComtemplateException {

	private String name;

	public ClassPathResolutionException(String name) {
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "cannot find class path <" + name + '>';
	}

}
