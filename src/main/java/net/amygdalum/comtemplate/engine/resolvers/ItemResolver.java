package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;

import java.math.BigInteger;
import java.util.List;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;

public class ItemResolver extends FunctionResolver {

	public ItemResolver() {
		super("item", 1);
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (base instanceof ResolvedListLiteral) {
			List<TemplateImmediateExpression> list = ((ResolvedListLiteral) base).getList();
			int size = list.size();
			int index = arguments.get(0).as(BigInteger.class).intValue();
			if (size <= index) {
				return new ExpressionResolutionError(base, getName(), arguments, scope, this);
			}
			TemplateImmediateExpression element = list.get(index);
			if (element == null) {
				return new ExpressionResolutionError(base, getName(), arguments, scope, this);
			} else {
				return element;
			}
		} else {
			return new ExpressionResolutionError(base, getName(), arguments, scope, this);
		}
	}

	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(ResolvedListLiteral.class);
	}
}