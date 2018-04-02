package net.amygdalum.comtemplate.engine;

public class TemplateGroupNotFoundException extends ComtemplateException {

	private String name;
	private String fileName;

	public TemplateGroupNotFoundException(String name) {
		this.name = name;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public String getMessage() {
		return "cannot find template group <" + name + '>' + getContext();
	}

	private String getContext() {
		return fileName != null 
			? " in file '" + fileName + "'"
			: "";
	}

}
