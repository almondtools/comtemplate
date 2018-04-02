package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Optional;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.ResolvedMapLiteral;

public class MapDynamicResolver extends ExclusiveTypeResolver<ResolvedMapLiteral> {

	public MapDynamicResolver() {
		super(ResolvedMapLiteral.class);
	}

	@Override
	public TemplateImmediateExpression resolveTyped(ResolvedMapLiteral base, String attribute, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (arguments.isEmpty()) {
			return Optional.ofNullable(base.getAttribute(attribute))
				.orElseGet(() ->  new ExpressionResolutionError(base, attribute, arguments, scope, this));
		}
		return new ExpressionResolutionError(base, attribute, arguments, scope, this);
	}
	
	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(ResolvedMapLiteral.class);
	}

}
