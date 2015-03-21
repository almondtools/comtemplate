package com.almondtools.comtemplate.engine;



public class TemplateVariable {

	private String name;
	private TemplateExpression value;

	private TemplateVariable(String name, TemplateExpression value) {
		this.name = name;
		this.value = value;
	}
	
	public static TemplateVariable var(String name, TemplateExpression value) {
		return new TemplateVariable(name, value);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasName(String name) {
		return this.name.equals(name);
	}

	public TemplateExpression getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() * 17 + value.hashCode();
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
		TemplateVariable that = (TemplateVariable) obj;
		if (!name.equals(that.name)) {
			return false;
		}
		if (!value.equals(that.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return name + "=" + value.toString();
	}

}
