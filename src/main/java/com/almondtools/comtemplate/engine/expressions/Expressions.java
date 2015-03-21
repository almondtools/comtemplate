package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.bool;
import static com.almondtools.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import com.almondtools.comtemplate.engine.TemplateExpression;

public final class Expressions {

	private static final Set<Class<?>> FLOATS = new HashSet<Class<?>>(asList(Double.class, Float.class));
	private static final Set<Class<?>> INTS = new HashSet<Class<?>>(asList(Byte.class, Short.class, Integer.class, Long.class));

	private Expressions() {
	}

	public static TemplateExpression fromNative(Object value) {
		Class<? extends Object> valueClass = value.getClass();
		if (TemplateExpression.class.isAssignableFrom(valueClass)) {
			return TemplateExpression.class.cast(value);
		} else if (valueClass == String.class) {
			return string((String) value);
		} else if (valueClass == Character.class) {
			return string(value.toString());
		} else if (valueClass == Boolean.class) {
			return bool((Boolean) value);
		} else if (valueClass == BigDecimal.class) {
			return decimal((BigDecimal) value);
		} else if (valueClass == BigInteger.class) {
			return integer((BigInteger) value);
		} else if (FLOATS.contains(valueClass)) {
			return decimal(((Number) value).toString());
		} else if (INTS.contains(valueClass)) {
			return integer(((Number) value).toString());
		} else {
			return new NativeObject(value);
		}
	}

}
