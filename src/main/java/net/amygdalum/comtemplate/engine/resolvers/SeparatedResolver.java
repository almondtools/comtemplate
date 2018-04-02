package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.RawText;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;

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
	
	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(ResolvedListLiteral.class);
	}
	
}
