package com.almondtools.comtemplate.engine;

import java.util.HashMap;
import java.util.Map;

import com.almondtools.comtemplate.engine.templates.ApplyTemplate;
import com.almondtools.comtemplate.engine.templates.ForTemplate;
import com.almondtools.comtemplate.engine.templates.IfTemplate;

public class GlobalTemplates {

	private Map<String, TemplateDefinition> templates;
	private Map<String, TemplateVariable> globals;

	public GlobalTemplates() {
		this.templates = new HashMap<>();
		this.globals = new HashMap<>();
	}
	
	public void register(TemplateDefinition definition) {
		templates.put(definition.getName(), definition);
	}

	public void register(TemplateVariable global) {
		globals.put(global.getName(), global);
	}

	public void register(String name, TemplateExpression global) {
		register(TemplateVariable.var(name, global));
	}

	public TemplateDefinition resolve(String name) {
		return templates.get(name);
	}

	public TemplateVariable resolveGlobal(String name) {
		return globals.get(name);
	}

	public static GlobalTemplates defaultTemplates() {
		GlobalTemplates templates = new GlobalTemplates();

		templates.register(new IfTemplate());
		templates.register(new ForTemplate());
		templates.register(new ApplyTemplate());
		
		return templates;
	}

}
