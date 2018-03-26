package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateParameter.toParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TemplateGroup {

	public static final String NONE_ID = "<none>";
	
	public static final TemplateGroup NONE = new TemplateGroup(NONE_ID, NONE_ID);
	
	private String name;
	private String resource;
	private List<TemplateDefinition> imports;
	private List<TemplateDefinition> definitions;

	public TemplateGroup(String name, String resource) {
		this.name = name;
		this.resource = resource;
		this.imports = new ArrayList<>();
		this.definitions = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public String getResource() {
		return resource;
	}

	public String relativeReference(String reference) {
		int lastSlash = name.lastIndexOf('.');
		String base = name.substring(0, lastSlash + 1);
		return base + reference;
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

	public Optional<TemplateVariable> resolveVariable(String variable) {
		return Stream.concat(definitions.stream(),imports.stream())
			.filter(def -> def instanceof ValueDefinition)
			.map(def -> ((ValueDefinition) def).toVariable())
			.filter(constant -> variable.equals(constant.getName()))
			.findFirst();
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
