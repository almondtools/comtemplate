package com.almondtools.comtemplate.engine;

import static java.util.Arrays.asList;

import java.util.List;

public class Scope {

	private Scope parent;
	private TemplateDefinition definition;
	private List<TemplateVariable> variables;

	public Scope(Scope parent, TemplateDefinition definition, List<TemplateVariable> variables) {
		this.parent = parent;
		this.definition = definition;
		this.variables = variables;
	}

	public Scope(Scope parent, TemplateDefinition definition, TemplateVariable... variables) {
		this(parent, definition, asList(variables));
	}

	public Scope(TemplateDefinition definition, List<TemplateVariable> variables) {
		this.parent = null;
		this.definition = definition;
		this.variables = variables;
	}

	public Scope(TemplateDefinition definition, TemplateVariable... variables) {
		this(definition, asList(variables));
	}

	public Scope getParent() {
		return parent;
	}

	public TemplateDefinition getDefinition() {
		return definition;
	}

	public List<TemplateVariable> getVariables() {
		return variables;
	}

	public TemplateVariable resolveVariable(String name) {
		return variables.stream()
			.filter(variable -> variable.hasName(name))
			.findFirst()
			.orElseGet(() -> {
				TemplateGroup group = definition.getGroup();
				if (group == null) {
					return null;
				}
				return group.resolveVariable(name);
			});
	}

	public TemplateVariable resolveVariable(String name, TemplateDefinition definition) {
		if (this.definition == definition) {
			return resolveVariable(name);
		} else if (parent == null) {
			return null;
		} else {
			return parent.resolveVariable(name, definition);
		}
	}

	public TemplateVariable resolveContextVariable(String name) {
		TemplateVariable value = resolveVariable(name);
		Scope current = parent;
		while (value == null && current != null) {
			value = current.resolveVariable(name);
			current = current.parent;
		}
		return value;
	}

	public TemplateDefinition resolveTemplate(String template) {
		TemplateGroup group = definition.getGroup();
		if (group == null) {
			return null;
		}
		return group.getDefinition(template);
	}

	public TemplateDefinition resolveTemplate(String template, TemplateDefinition definition) {
		if (this.definition == definition) {
			return resolveTemplate(template);
		} else if (parent == null) {
			return null;
		} else {
			return parent.resolveTemplate(template, definition);
		}
	}

}
