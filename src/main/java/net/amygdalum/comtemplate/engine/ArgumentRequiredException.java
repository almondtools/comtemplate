package net.amygdalum.comtemplate.engine;


public class ArgumentRequiredException extends ComtemplateException {

	private String variable;

	public ArgumentRequiredException(String variable) {
		this.variable = variable;
	}

	@Override
	public String getMessage() {
		return "template evaluation failed:\n"
			+ "- argument <" + variable + "> is required";
	}
}
