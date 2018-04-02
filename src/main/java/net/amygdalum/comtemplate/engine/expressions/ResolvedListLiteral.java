package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;

public class ResolvedListLiteral implements TemplateImmediateExpression {

	private List<TemplateImmediateExpression> list;

	public ResolvedListLiteral(List<TemplateImmediateExpression> list) {
		this.list = list;
	}

	public ResolvedListLiteral(TemplateImmediateExpression... list) {
		this(asList(list));
	}
	
	public TemplateImmediateExpression getElement(int index) {
		if (index >= 0 && index < list.size()) {
			return list.get(index);
		} else {
			return null;
		}
	}

	public List<TemplateImmediateExpression> getList() {
		return list;
	}

	@Override
	public String getText() {
		return list.stream()
			.map(element -> element.getText())
			.collect(joining(", ", "[", "]"));
	}

	@Override
	public <T> T as(Class<T> clazz) {
		return null;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitListLiteral(this, scope);
	}

	@Override
	public int hashCode() {
		return list.hashCode();
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
		ResolvedListLiteral that = (ResolvedListLiteral) obj;
		return this.list.equals(that.list);
	}

	@Override
	public String toString() {
		return list.stream()
			.map(item -> item.toString())
			.collect(joining(",", "[", "]"));
	}

}
