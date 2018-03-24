package com.almondtools.comtemplate.engine.expressions;

import static java.util.stream.Collectors.joining;

import com.almondtools.comtemplate.engine.TemplateDefinition;

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
		if (definition != null && definition.getGroup() != null) {
			buffer.append("\naccessed in <").append(definition.getName()).append('>');
			buffer.append("\navailable templates: ");
			buffer.append(definition.getGroup().getDefinitions().stream()
				.map(definition -> definition.getName())
				.collect(joining(",")));

		}
		return buffer.toString();
	}

}
