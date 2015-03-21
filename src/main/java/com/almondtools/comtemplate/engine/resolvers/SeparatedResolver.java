package com.almondtools.comtemplate.engine.resolvers;

import static java.util.stream.Collectors.joining;

import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.RawText;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;

public class SeparatedResolver extends FunctionResolver {

	public SeparatedResolver() {
		super("separated", 1);
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (base instanceof ResolvedListLiteral) {
			String separator = arguments.get(0).getText();
			String text = ((ResolvedListLiteral) base).getList().stream()
				.map(expression -> expression.getText())
				.collect(joining(separator));
			return new RawText(text);
		} else {
			return base;
		}
	}

	
}
