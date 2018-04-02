package net.amygdalum.comtemplate.engine.expressions;

import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;

public class UnexpectedTypeError extends ErrorExpression {

	private String required;
	private TemplateImmediateExpression found;

	public UnexpectedTypeError(String required, TemplateImmediateExpression found) {
		this.required = required;
		this.found = found;
	}

	@Override
	public String getMessage() {
		return "expected expression evaluable to <" + required + "> but found <" + found + ">";
	}

}
