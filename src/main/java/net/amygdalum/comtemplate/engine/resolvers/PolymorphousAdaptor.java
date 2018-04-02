package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.amygdalum.comtemplate.engine.Resolver;
import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.ResolvedMapLiteral;
import net.amygdalum.comtemplate.engine.expressions.ToObject;

public abstract class PolymorphousAdaptor implements Resolver {

	private String name;
	private Map<String, MonomophousAdaptor> adaptors;

	public PolymorphousAdaptor(String name, MonomophousAdaptor... adaptors) {
		this.name = name;
		this.adaptors = Arrays.stream(adaptors).collect(toMap(MonomophousAdaptor::getType, Function.identity()));
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (name.equals(function) && base instanceof ResolvedMapLiteral) {
			ResolvedMapLiteral object = (ResolvedMapLiteral) base;
			String type = object.getAttribute(ToObject.TYPE).getText();
			MonomophousAdaptor adaptor = adaptors.get(type);
			if (adaptor != null) {
				try {
					return adaptor.resolve(object, arguments, scope);
				} catch (RuntimeException e) {
					return new ExpressionResolutionError(base, name, arguments, scope, this);
				}
			}
		} 
		return new ExpressionResolutionError(base, name, arguments, scope, this);
	}

	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(ResolvedMapLiteral.class);
	}

}
