package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.TemplateParameter.param;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.MapLiteral.map;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import com.almondtools.comtemplate.engine.ArgumentRequiredException;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateVariable;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;
import com.almondtools.comtemplate.engine.expressions.TemplateResolutionError;
import com.almondtools.comtemplate.engine.expressions.UnexpectedTypeError;

public class ApplyTemplate extends TemplateDefinition {

	public static final String NAME = "apply";
	public static final String TEMPLATE_NAME = "name";
	public static final String TEMPLATE_ARGUMENTS = "arguments";

	public ApplyTemplate() {
		super(NAME, TEMPLATE_NAME, param(TEMPLATE_ARGUMENTS, map()));
	}

	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		TemplateVariable name = findVariable(TEMPLATE_NAME, arguments)
			.orElseThrow(() -> new ArgumentRequiredException(TEMPLATE_NAME));
		TemplateImmediateExpression evaluated = name.getValue().apply(interpreter, parent);
		String value = evaluated.as(String.class);
		if (value == null) {
			return new UnexpectedTypeError("boolean", evaluated);
		}
		TemplateVariable args = getParameter(TEMPLATE_ARGUMENTS).from(arguments);
		ResolvedMapLiteral resolvedArgumentMap = Optional.of(args.getValue().apply(interpreter, parent))
			.filter(expression -> expression instanceof ResolvedMapLiteral)
			.map(expression -> (ResolvedMapLiteral) expression)
			.orElse(new ResolvedMapLiteral());
		List<TemplateVariable> resolvedArguments = resolvedArgumentMap.getMap().entrySet().stream()
			.map(entry -> var(entry.getKey(), entry.getValue()))
			.collect(toList());

		if (parent == null) {
			return new TemplateResolutionError(value, this);
		}
		TemplateDefinition resolvedTemplate = parent.resolveTemplate(value);
		if (resolvedTemplate == null) {
			return new TemplateResolutionError(value, parent.getDefinition());
		}
		Scope scope = new Scope(parent, this, arguments);
		return resolvedTemplate.evaluate(interpreter, scope, resolvedArguments);
	}

}
