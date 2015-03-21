package com.almondtools.comtemplate.engine.expressions;

import java.math.BigInteger;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;


public class IntegerLiteral implements TemplateImmediateExpression {

	private BigInteger value;

	private IntegerLiteral(BigInteger value) {
		this.value = value;
	}
	
	public static IntegerLiteral integer(BigInteger value) {
		return new IntegerLiteral(value);
	}
	
	public static IntegerLiteral integer(String value) {
		return new IntegerLiteral(new BigInteger(value));
	}
	
	public static IntegerLiteral integer(long value) {
		return new IntegerLiteral(BigInteger.valueOf(value));
	}

	public BigInteger getValue() {
		return value;
	}

	@Override
	public String getText() {
		return value.toString();
	}

	@Override
	public <T> T as(Class<T> clazz) {
		if (BigInteger.class == clazz) {
			return clazz.cast(value);
		} else if (Long.class == clazz) {
			return clazz.cast(value.longValue());
		} else if (Integer.class == clazz) {
			return clazz.cast(value.intValue());
		} else if (Short.class == clazz) {
			return clazz.cast(value.shortValue());
		} else if (Byte.class == clazz) {
			return clazz.cast(value.byteValue());
		}
		return null;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitIntegerLiteral(this, scope);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		IntegerLiteral that = (IntegerLiteral) obj;
		return this.value.equals(that.value);
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

}
