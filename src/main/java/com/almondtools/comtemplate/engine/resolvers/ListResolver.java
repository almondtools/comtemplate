package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ErrorExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;

public class ListResolver extends AbstractResolver<ResolvedListLiteral> {

	private static final String ITEM = "item";
	private static final String SIZE = "size";
	private static final String FIRST = "first";
	private static final String REST = "rest";
	private static final String LAST = "last";
	private static final String TRUNC = "trunc";
	private static final String STRIP = "strip";

	public ListResolver() {
		super(ResolvedListLiteral.class);
	}

	@Override
	public TemplateImmediateExpression resolveTyped(ResolvedListLiteral base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
		List<TemplateImmediateExpression> list = base.getList();
		int size = list.size();
		if (ITEM.equals(function)) {
			if (size == 0) {
				return new ExpressionResolutionError(base, function, arguments, scope, this);
			}
			int index = arguments.get(0).as(BigInteger.class).intValue();
			TemplateImmediateExpression element = base.getElement(index);
			if (element == null) {
				return new ExpressionResolutionError(base, function, arguments, scope, this);
			} else {
				return element;
			}
		} else if (SIZE.equals(function)) {
			return integer(size);
		} else if (FIRST.equals(function)) {
			if (size == 0) {
				return new ExpressionResolutionError(base, function, arguments, scope, this);
			} else {
				return list.get(0);
			}
		} else if (REST.equals(function)) {
			if (size == 0) {
				return new ExpressionResolutionError(base, function, arguments, scope, this);
			} else {
				return new ResolvedListLiteral(list.subList(1, size));
			}
		} else if (LAST.equals(function)) {
			if (size == 0) {
				return new ExpressionResolutionError(base, function, arguments, scope, this);
			} else {
				return list.get(size - 1);
			}
		} else if (TRUNC.equals(function)) {
			if (size == 0) {
				return new ExpressionResolutionError(base, function, arguments, scope, this);
			} else {
				return new ResolvedListLiteral(list.subList(0, size - 1));
			}
		} else if (STRIP.equals(function)) {
			if (size == 0) {
				return base;
			} else {
				List<TemplateImmediateExpression> stripped = list.stream()
					.filter(item -> !(item instanceof ErrorExpression))
					.collect(Collectors.toList());
				return new ResolvedListLiteral(stripped);
			}
		} else {
			return new ExpressionResolutionError(base, function, arguments, scope, this);
		}
	}

}
