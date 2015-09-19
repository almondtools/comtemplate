package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.bool;
import static com.almondtools.comtemplate.engine.resolvers.Normalizations.compact;
import static java.lang.Boolean.TRUE;

import java.util.Collection;
import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.Evaluated;
import com.almondtools.comtemplate.engine.expressions.NativeObject;
import com.almondtools.comtemplate.engine.expressions.RawText;
import com.almondtools.comtemplate.engine.expressions.StringLiteral;

public class EmptyResolver extends FunctionResolver {

	public EmptyResolver() {
		super("empty");
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (base instanceof StringLiteral) {
			return bool(((StringLiteral) base).getText().isEmpty());
		} else if (base instanceof RawText) {
			return bool(((RawText) base).getText().isEmpty());
		} else if (base instanceof Evaluated) {
			List<TemplateImmediateExpression> evaluated = compact(((Evaluated) base).getEvaluated());
			return bool(evaluated.stream()
				.map(expression -> resolve(expression, arguments, scope).as(Boolean.class))
				.reduce(TRUE, (b1, b2) -> b1 && b2));
		} else if (base instanceof NativeObject) {
			Object object = ((NativeObject) base).getObject();
			if (object instanceof String) {
				return bool(((String) object).isEmpty());
			} else if (object instanceof Collection<?>) {
				return bool(((Collection<?>) object).stream()
					.map(expression -> resolve(new NativeObject(expression), arguments, scope).as(Boolean.class))
					.reduce(TRUE, (b1, b2) -> b1 && b2));
			} else {
				return bool(false);
			}
		} else {
			return bool(false);
		}
	}

}
