package com.almondtools.comtemplate.engine;

import static java.util.stream.Collectors.joining;

import java.util.List;

public class TemplateGroupException extends ComtemplateException {

	private String name;
	private String fileName;
	private List<String> messages;

	public TemplateGroupException(String name, List<String> messages) {
		this.name = name;
		this.messages = messages;
	}

	public TemplateGroupException(String name, String fileName, List<String> messages) {
		this.name = name;
		this.fileName = fileName;
		this.messages = messages;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getMessage() {
		if (fileName != null) {
			return "parsing template group '" + name + "' in file '" + fileName + "' failed:\n"
				+ messages.stream().collect(joining("\n- ", "- ", ""));
		} else {
			return "parsing template group '" + name + "' failed:\n"
				+ messages.stream().collect(joining("\n- ", "- ", ""));
		}
	}

}
