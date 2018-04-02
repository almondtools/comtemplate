package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.NativeObject;

public class BeanDynamicResolver extends ExclusiveTypeResolver<NativeObject> {

	public BeanDynamicResolver() {
		super(NativeObject.class);
	}

	@Override
	public TemplateImmediateExpression resolveTyped(NativeObject base, String attribute, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (arguments.isEmpty()) {
			try {
				Object object = ((NativeObject) base).getObject();
				Class<?> clazz = object.getClass();
				Method propertyMethod = clazz.getDeclaredMethod(getterFor(attribute));
				Object result = propertyMethod.invoke(object);
				return new NativeObject(result);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				return new ExpressionResolutionError(base, attribute, arguments, scope, this);
			}
		} else {
			return new ExpressionResolutionError(base, attribute, arguments, scope, this);
		}
	}

	private String getterFor(String attribute) {
		return "get" + Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
	}
	
	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(NativeObject.class);
	}

}
