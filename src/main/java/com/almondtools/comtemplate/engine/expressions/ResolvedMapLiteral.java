package com.almondtools.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.TemplateVariable;

public class ResolvedMapLiteral implements TemplateImmediateExpression {

	private Map<String, TemplateImmediateExpression> map;

	public ResolvedMapLiteral(Map<String, TemplateImmediateExpression> map) {
		this.map = map;
	}

	public ResolvedMapLiteral(List<TemplateVariable> entries) {
		this(variableMap(entries));
	}

    private static Map<String, TemplateImmediateExpression> variableMap(List<TemplateVariable> entries) {
        return entries.stream()
			.collect(toMap(entry -> entry.getName(), entry -> (TemplateImmediateExpression) entry.getValue(), (o, n) -> n, LinkedHashMap::new));
    }

	public ResolvedMapLiteral(TemplateVariable... entries) {
		this(asList(entries));
	}

	public TemplateImmediateExpression getAttribute(String attribute) {
		return map.get(attribute);
	}

	public Map<String, TemplateImmediateExpression> getMap() {
		return map;
	}

	@Override
	public String getText() {
		return map.entrySet().stream()
			.map(entry -> entry.getKey() + "=" + entry.getValue().getText())
			.collect(joining(", ", "[", "]"));
	}

	@Override
	public <T> T as(Class<T> clazz) {
		TemplateImmediateExpression value = map.get(ToObject.VALUE);
		if (value == null) {
			return null;
		}
		return value.as(clazz);
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitMapLiteral(this, scope);
	}

	@Override
	public int hashCode() {
		return map.hashCode();
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
		ResolvedMapLiteral that = (ResolvedMapLiteral) obj;
		return this.map.equals(that.map);
	}

	@Override
	public String toString() {
		return map.entrySet().stream()
			.map(entry -> entry.getKey() + "=" + entry.getValue().toString())
			.collect(joining(",", "[", "]"));
	}

}
