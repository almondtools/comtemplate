package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateParameter.toParams;

import java.util.ArrayList;
import java.util.List;

public class TemplateGroup {

	private String name;
	private List<TemplateDefinition> imports;
	private List<TemplateDefinition> definitions;

	public TemplateGroup(String name) {
		this.name = name;
		this.imports = new ArrayList<>();
		this.definitions = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void addImport(TemplateDefinition definition) {
		imports.add(definition);
	}

	public void addImports(List<TemplateDefinition> definitions) {
		imports.addAll(definitions);
	}

	public List<TemplateDefinition> getImports() {
		return imports;
	}

	public List<TemplateDefinition> getDefinitions() {
		return definitions;
	}

	public TemplateDefinition getDefinition(String template) {
		return definitions.stream()
			.filter(def -> template.equals(def.getName()))
			.findFirst()
			.orElseGet(() -> imports.stream()
				.filter(def -> template.equals(def.getName()))
				.findFirst()
				.orElse(null));
	}

	public TemplateVariable resolveVariable(String variable) {
		return definitions.stream()
			.filter(def -> def instanceof ValueDefinition)
			.map(def -> ((ValueDefinition) def).toVariable())
			.filter(constant -> variable.equals(constant.getName()))
			.findFirst()
			.orElse(null);
	}

	public CustomTemplateDefinition defineTemplate(String name, List<TemplateParameter> parameters) {
		return define(new CustomTemplateDefinition(name, parameters));
	}

	public CustomTemplateDefinition defineTemplate(String name, Object... parameters) {
		return defineTemplate(name, toParams(parameters));
	}

	public ValueDefinition defineObject(String name, List<TemplateParameter> parameters) {
		return define(new ValueDefinition(name, parameters));
	}

	public ValueDefinition defineObject(String name, Object... parameters) {
		return defineObject(name, toParams(parameters));
	}

	public ValueDefinition defineConstant(String name) {
		return define(new ValueDefinition(name));
	}

	public <T extends TemplateDefinition> T define(T definition) {
		definition.setGroup(this);
		definitions.add(definition);
		return definition;
	}

}
