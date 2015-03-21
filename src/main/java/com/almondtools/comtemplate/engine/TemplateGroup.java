package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateParameter.toParams;

import java.util.ArrayList;
import java.util.List;

public class TemplateGroup {

	private String name;
	private List<TemplateDefinition> imports;
	private List<TemplateDefinition> definitions;
	private List<TemplateVariable> constants;

	public TemplateGroup(String name) {
		this.name = name;
		this.imports = new ArrayList<>();
		this.definitions = new ArrayList<>();
		this.constants = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void addImport(TemplateDefinition definition) {
		imports.add(definition);
	}

	public List<TemplateDefinition> getImports() {
		return imports;
	}

	public List<TemplateDefinition> getDefinitions() {
		return definitions;
	}

	public List<TemplateVariable> getConstants() {
		return constants;
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
		return constants.stream()
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

	public CustomObjectDefinition defineObject(String name, List<TemplateParameter> parameters) {
		return define(new CustomObjectDefinition(name, parameters));
	}

	public CustomObjectDefinition defineObject(String name, Object... parameters) {
		return defineObject(name, toParams(parameters));
	}

	public void defineConstant(TemplateVariable constant) {
		constants.add(constant);
	}

	public <T extends TemplateDefinition> T define(T definition) {
		definition.setGroup(this);
		definitions.add(definition);
		return definition;
	}

}
