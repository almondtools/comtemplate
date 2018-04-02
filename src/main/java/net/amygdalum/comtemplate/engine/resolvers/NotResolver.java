package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;

import java.util.List;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.BooleanLiteral;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;

public class NotResolver extends FunctionResolver {

	public NotResolver() {
		super("not");
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (base instanceof BooleanLiteral) {
			return ((BooleanLiteral) base).invert();
		} else {
			return new ExpressionResolutionError(base, "not", arguments, scope, this);
		}
	}
	
	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(BooleanLiteral.class);
	}
	
}
