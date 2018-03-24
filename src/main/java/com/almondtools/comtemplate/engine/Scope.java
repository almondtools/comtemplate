package com.almondtools.comtemplate.engine;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Optional;

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

	public Optional<TemplateVariable> resolveVariable(String name) {
		Optional<TemplateVariable> resolved = variables.stream()
			.filter(variable -> variable.hasName(name))
			.findFirst();
		if (resolved.isPresent()) {
			return resolved;
		}
		TemplateGroup group = definition.getGroup();
		if (group == null) {
			return Optional.empty();
		}
		return group.resolveVariable(name);
	}

	public Optional<TemplateVariable> resolveVariable(String name, TemplateDefinition definition) {
		if (this.definition == definition) {
			return resolveVariable(name);
		} else if (parent == null) {
			return Optional.empty();
		} else {
			return parent.resolveVariable(name, definition);
		}
	}

	public Optional<TemplateVariable> resolveContextVariable(String name) {
		Optional<TemplateVariable> value = resolveVariable(name);
		Scope current = parent;
		while (!value.isPresent() && current != null) {
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

	public TemplateDefinition resolveValue(TemplateDefinition definition, String template) {
		TemplateGroup group = definition.getGroup();
		if (group == null) {
			return null;
		}
		return group.getDefinition(template);
	}

	public TemplateDefinition resolveTemplate(String template, TemplateDefinition definition) {
		if (definition instanceof ValueDefinition) {
			return resolveValue(definition, template);
		} else if (this.definition == definition) {
			return resolveTemplate(template);
		} else if (parent == null) {
			return null;
		} else {
			return parent.resolveTemplate(template, definition);
		}
	}

}
