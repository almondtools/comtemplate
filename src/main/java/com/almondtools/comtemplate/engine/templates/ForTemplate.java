package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.Evaluated.assembling;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.util.stream.ByIndex.byIndex;
import static java.util.Arrays.asList;

import java.util.List;

import com.almondtools.comtemplate.engine.ArgumentRequiredException;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateVariable;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.UnexpectedTypeError;

public class ForTemplate extends TemplateDefinition {

	public static final String NAME = "for";
	public static final String VAR = "var";
	public static final String IVAR = "ivar";
	public static final String I = "i";
	public static final String I0 = "i0";
	public static final String DO = "do";

	public ForTemplate() {
		super(NAME, VAR, DO);
	}

	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		TemplateVariable var = findVariable(VAR, arguments)
			.orElseThrow(() -> new ArgumentRequiredException(VAR));
		TemplateVariable in = findVariable(DO, arguments)
			.orElseThrow(() -> new ArgumentRequiredException(DO));
		TemplateImmediateExpression evaluated = var.getValue().apply(interpreter, parent);
		Scope scope = new Scope(parent, this, arguments);
		if (evaluated instanceof ResolvedListLiteral) {
			List<TemplateImmediateExpression> list = ((ResolvedListLiteral) evaluated).getList();
			return list.stream()
				.map(byIndex(list.size()))
				.map(item -> {
					TemplateVariable ivar = var(IVAR, item.value);
					TemplateVariable i = var(I, integer(item.index + 1));
					TemplateVariable i0 = var(I0, integer(item.index));
					Scope iscope = new Scope(scope, this, asList(i, i0, ivar));
					return in.getValue().apply(interpreter, iscope);
				})
				.collect(assembling());
		} else {
			return new UnexpectedTypeError("list", evaluated);
		}
	}

}
