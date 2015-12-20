package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.bool;

import java.util.List;

import com.almondtools.comtemplate.engine.ArgumentRequiredException;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateVariable;
import com.almondtools.comtemplate.engine.expressions.BooleanLiteral;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.UnexpectedTypeError;

public class AnyTemplate extends TemplateDefinition {

	public static final String NAME = "any";

	private static final String COND = "cond";

	public AnyTemplate() {
		super(NAME, COND);
	}

	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		List<TemplateVariable> variables = createVariables(arguments);
		TemplateVariable cond = findVariable(COND, variables)
			.orElseThrow(() -> new ArgumentRequiredException(COND));
		TemplateImmediateExpression evaluated = cond.getValue().apply(interpreter, parent);
		if (evaluated instanceof BooleanLiteral) {
			return evaluated;
		} else if (evaluated instanceof ResolvedListLiteral) {
			boolean any = false;
			for (TemplateImmediateExpression part : ((ResolvedListLiteral) evaluated).getList()) {
				Boolean value = part.as(Boolean.class);
				if (value == null) {
					return new UnexpectedTypeError("boolean", evaluated);
				}
				any |= value;
			}
			return bool(any);
		} else {
			return new UnexpectedTypeError("boolean", evaluated);
		}
	}

}
