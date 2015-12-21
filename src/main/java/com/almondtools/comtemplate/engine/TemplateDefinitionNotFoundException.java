package com.almondtools.comtemplate.engine;

public class TemplateDefinitionNotFoundException extends ComtemplateException {

	private String group;
	private String name;

	public TemplateDefinitionNotFoundException(String group, String name) {
		this.group = group;
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "cannot find template definition <" + name + "> in <" + group + '>';
	}

}
