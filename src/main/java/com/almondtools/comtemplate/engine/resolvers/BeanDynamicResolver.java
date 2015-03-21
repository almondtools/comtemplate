package com.almondtools.comtemplate.engine.resolvers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.NativeObject;

public class BeanDynamicResolver extends AbstractResolver<NativeObject> {

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

}
