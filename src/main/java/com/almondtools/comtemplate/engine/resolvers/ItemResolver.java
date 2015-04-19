package com.almondtools.comtemplate.engine.resolvers;

import java.math.BigInteger;
import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;

public class ItemResolver extends FunctionResolver {

	public ItemResolver() {
		super("item", 1);
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (base instanceof ResolvedListLiteral) {
			List<TemplateImmediateExpression> list = ((ResolvedListLiteral) base).getList();
			int size = list.size();
			if (size == 0) {
				return new ExpressionResolutionError(base, getName(), arguments, scope, this);
			}
			int index = arguments.get(0).as(BigInteger.class).intValue();
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

}