package net.amygdalum.comtemplate.engine;

import static net.amygdalum.comtemplate.engine.TemplateVariable.var;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import net.amygdalum.comtemplate.engine.templates.AllTemplate;
import net.amygdalum.comtemplate.engine.templates.AnyTemplate;
import net.amygdalum.comtemplate.engine.templates.ApplyTemplate;
import net.amygdalum.comtemplate.engine.templates.ForTemplate;
import net.amygdalum.comtemplate.engine.templates.IfTemplate;
import net.amygdalum.comtemplate.engine.templates.NotTemplate;

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
		register(var(name, global));
	}

	public TemplateDefinition resolve(String name) {
		return templates.get(name);
	}

	public Optional<TemplateVariable> resolveGlobal(String name) {
		return Optional.ofNullable(globals.get(name));
	}

	public static GlobalTemplates defaultTemplates() {
		GlobalTemplates templates = new GlobalTemplates();

		templates.register(new IfTemplate());
		templates.register(new ForTemplate());
		templates.register(new ApplyTemplate());
		templates.register(new NotTemplate());
		templates.register(new AllTemplate());
		templates.register(new AnyTemplate());
		
		ServiceLoader<TemplateDefinition> definitionService = ServiceLoader.load(TemplateDefinition.class);
		for (TemplateDefinition definition : definitionService) {
			templates.register(definition);
		}
		return templates;
	}

}
