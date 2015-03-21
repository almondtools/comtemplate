package com.almondtools.comtemplate.engine.expressions;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpression;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;
import com.almondtools.comtemplate.engine.TemplateVariable;

public class MapLiteral implements TemplateExpression {

	private Map<String, TemplateExpression> map;
	
	private MapLiteral(Map<String, TemplateExpression> map) {
		this.map = map;
	}
	
	public static MapLiteral map(Map<String, TemplateExpression> map) {
		return new MapLiteral(map);
	}

	public static MapLiteral map(TemplateVariable... entries) {
		Map<String, TemplateExpression> map = Stream.of(entries)
			.collect(toMap(TemplateVariable::getName, TemplateVariable::getValue, (o,n)->n, LinkedHashMap::new));
		return map(map);
	}

	public Map<String, TemplateExpression> getMap() {
		return map;
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
		MapLiteral that = (MapLiteral) obj;
		return this.map.equals(that.map);
	}

	@Override
	public String toString() {
		return map.entrySet().stream()
			.map(entry -> entry.getKey() + "=" + entry.getValue().toString())
			.collect(joining(",", "[", "]"));
	}

}
