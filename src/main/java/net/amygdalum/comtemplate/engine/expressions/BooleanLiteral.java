package net.amygdalum.comtemplate.engine.expressions;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;


public class BooleanLiteral implements TemplateImmediateExpression {

	public static final BooleanLiteral FALSE = new BooleanLiteral(false);
	public static final BooleanLiteral TRUE = new BooleanLiteral(true);
	
	private Boolean value;

	private BooleanLiteral(Boolean value) {
		this.value = value;
	}
	
	public static BooleanLiteral bool(Boolean value) {
		if (value == null) {
			throw new IllegalArgumentException("cannot evaluate boolean: null");
		} else if (value)  {
			return TRUE;
		} else {
			return FALSE;
		}
	}
	
	public static BooleanLiteral bool(String value) {
		return bool(Boolean.valueOf(value));
	}
	
	public BooleanLiteral invert() {
		return bool(!value);
	}
	
	public Boolean getValue() {
		return value;
	}

	@Override
	public String getText() {
		return value.toString();
	}
	
	@Override
	public <T> T as(Class<T> clazz) {
		if (Boolean.class == clazz) {
			return clazz.cast(value);
		}
		return null;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitBooleanLiteral(this, scope);
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
		BooleanLiteral that = (BooleanLiteral) obj;
		return this.value.equals(that.value);
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

}
