package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;

import java.util.List;

import net.amygdalum.comtemplate.engine.Resolver;
import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;

public abstract class ExclusiveTypeResolver<T extends TemplateImmediateExpression> implements Resolver {

	private Class<T> clazz;

	public ExclusiveTypeResolver(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (clazz.isInstance(base)) {
			return resolveTyped(clazz.cast(base), function, arguments, scope);
		}
		return new ExpressionResolutionError(base, function, arguments, scope, this);
	}

	public abstract TemplateImmediateExpression resolveTyped(T base, String function, List<TemplateImmediateExpression> arguments, Scope scope);
	
	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(clazz);
	}
}
