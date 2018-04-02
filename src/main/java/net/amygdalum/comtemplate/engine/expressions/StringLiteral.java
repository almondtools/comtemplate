package net.amygdalum.comtemplate.engine.expressions;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;


public class StringLiteral implements TemplateImmediateExpression {

	private String value;

	private StringLiteral(String value) {
		this.value = value;
	}
	
	public static StringLiteral string(String value) {
		return new StringLiteral(value);
	}

	public String getValue() {
		return value;
	}

	@Override
	public String getText() {
		return value;
	}

	@Override
	public <T> T as(Class<T> clazz) {
		if (String.class == clazz) {
			return clazz.cast(value);
		}
		return null;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitStringLiteral(this, scope);
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
		StringLiteral that = (StringLiteral) obj;
		return this.value.equals(that.value);
	}
	
	@Override
	public String toString() {
		return '\'' + value + '\'';
	}
}
