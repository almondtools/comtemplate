package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import com.almondtools.comtemplate.engine.expressions.Cast;

public class TemplateParameter {

	private String name;
	private String type;
	private TemplateExpression defaultValue;

	private TemplateParameter(String name, String type, TemplateExpression defaultValue) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	public static TemplateParameter param(String name) {
		return new TemplateParameter(name, null, null);
	}

	public static TemplateParameter param(String name, TemplateExpression defaultValue) {
		return new TemplateParameter(name, null, defaultValue);
	}

	public static TemplateParameter param(String name, String type, TemplateExpression defaultValue) {
		return new TemplateParameter(name, type, defaultValue);
	}

	private static TemplateParameter toParam(Object templateParameter) {
		if (templateParameter instanceof TemplateParameter) {
			return (TemplateParameter) templateParameter;
		} else if (templateParameter instanceof String) {
			return param((String) templateParameter);
		} else {
			throw new IllegalArgumentException("cannot convert to template parameter:" + templateParameter);
		}
	}

	public static List<TemplateParameter> toParams(Object... templateParameters) {
		return Stream.of(templateParameters)
			.map(templateParameter -> toParam(templateParameter))
			.collect(toList());
	}

	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public TemplateExpression getDefaultValue() {
		return defaultValue;
	}

	public TemplateVariable from(List<TemplateVariable> arguments) {
		return arguments.stream()
			.filter(argument -> argument.getName().equals(name))
			.map(argument -> type == null ? argument : var(argument.getName(), new Cast(argument.getValue(), type)))
			.findFirst()
			.orElseGet(() -> createDefaultVariable());
	}

	private TemplateVariable createDefaultVariable() {
		if (defaultValue != null) {
			return var(name, defaultValue);
		} else {
			return null;
		}
	}

	@Override
	public int hashCode() {
		int hashCode = name.hashCode();
		if (defaultValue != null) {
			hashCode = hashCode * 31 + defaultValue.hashCode();
		}
		return hashCode;
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
		TemplateParameter that = (TemplateParameter) obj;
		if (!name.equals(that.name)) {
			return false;
		}
		if (defaultValue == that.defaultValue) {
			return true;
		} else if (defaultValue == null || that.defaultValue == null) {
			return false;
		} else if (!defaultValue.equals(that.defaultValue)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return name + "=>" + defaultValue.toString();
	}

}
