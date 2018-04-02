package net.amygdalum.comtemplate.engine.expressions;

import java.math.BigDecimal;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;

public class DecimalLiteral implements TemplateImmediateExpression {

	private BigDecimal value;

	private DecimalLiteral(BigDecimal value) {
		this.value = value;
	}

	public static DecimalLiteral decimal(BigDecimal value) {
		return new DecimalLiteral(value);
	}

	public static DecimalLiteral decimal(double value) {
		return new DecimalLiteral(BigDecimal.valueOf(value));
	}

	public static DecimalLiteral decimal(String value) {
		return new DecimalLiteral(new BigDecimal(value));
	}

	public BigDecimal getValue() {
		return value;
	}

	@Override
	public String getText() {
		return value.toString();
	}

	@Override
	public <T> T as(Class<T> clazz) {
		if (BigDecimal.class == clazz) {
			return clazz.cast(value);
		} else if (Double.class == clazz) {
			return clazz.cast(value.doubleValue());
		} else if (Float.class == clazz) {
			return clazz.cast((float) value.doubleValue());
		}
		return null;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitDecimalLiteral(this, scope);
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
		DecimalLiteral that = (DecimalLiteral) obj;
		return this.value.equals(that.value);
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
