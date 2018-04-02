package net.amygdalum.comtemplate.engine;

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

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getMessage() {
		return "parsing template group <" + name + ">" + getContext() + " failed:\n"
			+ messages.stream().collect(joining("\n- ", "- ", ""));
	}

	private String getContext() {
		return fileName != null 
			? " in file '" + fileName + "'"
			: "";
	}

}
