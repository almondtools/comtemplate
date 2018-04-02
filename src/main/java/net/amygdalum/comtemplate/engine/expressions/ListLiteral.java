package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;

public class ListLiteral implements TemplateExpression {

	private List<TemplateExpression> list;

	private ListLiteral(List<TemplateExpression> list) {
		this.list = list;
	}

	public static ListLiteral list(List<TemplateExpression> items) {
		return new ListLiteral(items);
	}

	public static ListLiteral list(TemplateExpression... items) {
		return list(asList(items));
	}

	public List<TemplateExpression> getList() {
		return list;
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
		ListLiteral that = (ListLiteral) obj;
		return this.list.equals(that.list);
	}

	@Override
	public String toString() {
		return list.stream()
			.map(item -> item.toString())
			.collect(joining(",", "[", "]"));
	}

}
