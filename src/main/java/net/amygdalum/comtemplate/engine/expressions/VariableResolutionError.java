package net.amygdalum.comtemplate.engine.expressions;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;

public class VariableResolutionError extends ErrorExpression {

	private String variable;
	private TemplateDefinition definition;
	private Scope scope;

	public VariableResolutionError(String variable, TemplateDefinition definition) {
		this.variable = variable;
		this.definition = definition;
	}

	public VariableResolutionError(String variable, Scope scope) {
		this.variable = variable;
		this.scope = scope;
		this.definition = scope.getDefinition();
	}
	
	@Override
	public String getMessage() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("variable <").append(variable).append("> cannot be resolved");
		if (definition != null)  {
			buffer.append("\naccessed in <").append(definition.getLocation()).append(">");
		}
		if (scope != null) {
			buffer.append('\n').append(getScopeStack(scope));
		}
		return buffer.toString();
	}

}
