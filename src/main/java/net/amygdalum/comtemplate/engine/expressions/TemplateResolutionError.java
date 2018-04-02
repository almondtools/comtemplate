package net.amygdalum.comtemplate.engine.expressions;

import static java.util.stream.Collectors.joining;

import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateGroup;

public class TemplateResolutionError extends ErrorExpression {

	private String template;
	private TemplateDefinition definition;

	public TemplateResolutionError(String template, TemplateDefinition definition) {
		this.template = template;
		this.definition = definition;
	}

	@Override
	public String getMessage() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("template <").append(template).append("> cannot be resolved");
		if (definition != null) {
			buffer.append("\naccessed in <").append(definition.getLocation()).append('>');
			TemplateGroup group = definition.getGroup();
			if (group != null) {
				buffer.append("\navailable templates: ");
				buffer.append(group.getDefinitions().stream()
					.map(definition -> definition.getName())
					.collect(joining(",")));
			}

		}
		return buffer.toString();
	}

}
