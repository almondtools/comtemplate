package net.amygdalum.comtemplate.engine.templates;

import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.bool;

import java.util.List;

import net.amygdalum.comtemplate.engine.ArgumentRequiredException;
import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.TemplateInterpreter;
import net.amygdalum.comtemplate.engine.TemplateVariable;
import net.amygdalum.comtemplate.engine.expressions.BooleanLiteral;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;
import net.amygdalum.comtemplate.engine.expressions.UnexpectedTypeError;

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
