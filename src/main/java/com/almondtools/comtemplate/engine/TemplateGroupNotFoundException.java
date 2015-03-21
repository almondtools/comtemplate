package com.almondtools.comtemplate.engine;

public class TemplateGroupNotFoundException extends ComtemplateException {

	private String name;

	public TemplateGroupNotFoundException(String name) {
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "cannot find template group <" + name + '>';
	}

}
