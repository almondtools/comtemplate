package net.amygdalum.comtemplate.engine;

import static java.util.Collections.emptyList;

import java.util.List;

import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;

public interface Resolver {

	public static final Resolver NULL = new Resolver() {

		@Override
		public TemplateImmediateExpression resolve(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
			return new ExpressionResolutionError(base, function, arguments, scope, null);
		}

		@Override
		public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
			return emptyList();
		}
	};

	TemplateImmediateExpression resolve(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope);

	List<Class<? extends TemplateImmediateExpression>> getResolvedClasses();

	
}
