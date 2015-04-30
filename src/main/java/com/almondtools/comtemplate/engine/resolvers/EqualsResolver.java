package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.bool;

import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;

public class EqualsResolver extends FunctionResolver {

	public EqualsResolver() {
		super("equals", 1);
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		String actual = base.getText();		
		String expected = arguments.get(0).getText();
		return bool(actual.equals(expected));
	}

}
