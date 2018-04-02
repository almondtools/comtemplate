package net.amygdalum.comtemplate.engine.expressions;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;

public class NativeObject implements TemplateImmediateExpression {

	private Object object;

	public NativeObject(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

	@Override
	public String getText() {
		return object.toString();
	}

	@Override
	public <T> T as(Class<T> clazz) {
		if (clazz.isInstance(object)) {
			return clazz.cast(object);
		}
		return null;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitNativeObject(this, scope);
	}
	
	@Override
	public int hashCode() {
		if (object == null) {
			return 0;
		}else {
			return object.hashCode();
		}
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
		NativeObject that = (NativeObject) obj;
		if (this.object == that.object) {
			return true;
		}
		if (this.object == null || that.object == null) {
			return false;
		}
		return this.object.equals(that.object);
	}

	@Override
	public String toString() {
		return "*'" + object.toString() + '\'';
	}

}
