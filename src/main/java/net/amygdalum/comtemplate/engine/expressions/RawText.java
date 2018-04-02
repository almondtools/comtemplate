package net.amygdalum.comtemplate.engine.expressions;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;

public class RawText implements TemplateImmediateExpression {

	public static final TemplateExpression EMPTY = new RawText("");
	
	private String text;

	public RawText(String text) {
		this.text = text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public <T> T as(Class<T> clazz) {
		if (String.class == clazz) {
			return clazz.cast(text);
		}
		return null;
	}

	public void join(RawText text) {
		this.text = this.text + text.text;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitRawText(this, scope);
	}

	@Override
	public int hashCode() {
		return text.hashCode();
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
		RawText that = (RawText) obj;
		return this.text.equals(that.text);
	}
	
	@Override
	public String toString() {
		return '\'' + text + '\'';
	}

}
