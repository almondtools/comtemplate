package net.amygdalum.comtemplate.engine.templates;

import static java.util.Arrays.asList;
import static net.amygdalum.comtemplate.engine.TemplateParameter.param;

import java.util.AbstractMap.SimpleEntry;

import net.amygdalum.comtemplate.engine.ArgumentRequiredException;
import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.TemplateInterpreter;
import net.amygdalum.comtemplate.engine.TemplateVariable;
import net.amygdalum.comtemplate.engine.expressions.RawText;
import net.amygdalum.comtemplate.engine.expressions.UnexpectedTypeError;

import java.util.List;

public class IfTemplate extends TemplateDefinition {

	public static final String NAME = "if";

	private static final String COND = "cond";
	private static final String THEN = "then";
	private static final String ELSE = "else";

	private static final List<SimpleEntry<String, Boolean>> CONDITIONS = asList(
		new SimpleEntry<String, Boolean>(THEN, true),
		new SimpleEntry<String, Boolean>(ELSE, false));

	public IfTemplate() {
		super(NAME, COND, THEN, param(ELSE, RawText.EMPTY));
	}

	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		List<TemplateVariable> variables = createVariables(arguments);
		TemplateVariable cond = findVariable(COND, variables)
			.orElseThrow(() -> new ArgumentRequiredException(COND));
		TemplateImmediateExpression evaluated = cond.getValue().apply(interpreter, parent);
		Boolean value = evaluated.as(Boolean.class);
		if (value == null) {
			return new UnexpectedTypeError("boolean", evaluated);
		}
		Scope scope = new Scope(parent, this, variables);
        return CONDITIONS.stream()
			.filter(condition -> condition.getValue().equals(value))
			.map(block -> findVariable(block.getKey(), variables).orElseThrow(() -> new ArgumentRequiredException(block.getKey())))
			.map(variable -> variable.getValue().apply(interpreter, scope))
			.findFirst()
			.get();
	}

}
