package net.amygdalum.comtemplate.engine;


public class ContextRequiredException extends ComtemplateException {

	private String variable;

	public ContextRequiredException(String variable) {
		this.variable = variable;
	}

	@Override
	public String getMessage() {
		return "context variable <" + variable + "> is required";
	}
}
