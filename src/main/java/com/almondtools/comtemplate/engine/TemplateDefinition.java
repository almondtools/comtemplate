package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateParameter.toParams;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.Expressions.fromNative;
import static com.almondtools.util.stream.ByIndex.byIndex;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class TemplateDefinition {

	private TemplateGroup group;
	private String name;
	private List<TemplateParameter> parameters;

	public TemplateDefinition(String name, List<TemplateParameter> parameters) {
		this.name = name;
		this.parameters = parameters;
	}

	public TemplateDefinition(String name, Object... parameters) {
		this(name, toParams(parameters));
	}

	public void setGroup(TemplateGroup group) {
		this.group = group;
	}

	public TemplateGroup getGroup() {
		return group;
	}

	public String getName() {
		return name;
	}

	public List<TemplateParameter> getParameters() {
		return parameters;
	}

	public TemplateParameter getParameter(String name) {
		return parameters.stream()
			.filter(param -> name.equals(param.getName()))
			.findFirst()
			.orElse(null);
	}

	public String evaluateNative(TemplateInterpreter interpreter, Object... arguments) {
		return evaluateNative(interpreter, asList(arguments));
	}

	public String evaluateNative(TemplateInterpreter interpreter, List<Object> arguments) {
		return evaluate(interpreter, toVariables(arguments));
	}

	private List<TemplateVariable> toVariables(List<Object> arguments) {
		int size = Math.max(arguments.size(), parameters.size());
		return arguments.stream()
			.limit(size)
			.map(byIndex(size))
			.map(item -> var(parameters.get(item.index).getName(), fromNative(item.value)))
			.collect(toList());
	}

	public String evaluate(TemplateInterpreter interpreter, TemplateVariable... arguments) {
		return evaluate(interpreter, asList(arguments));
	}

	public String evaluate(TemplateInterpreter interpreter, List<TemplateVariable> arguments) {
		TemplateImmediateExpression evaluated = evaluate(interpreter, null, arguments);
			
		return evaluated.getText();
	}

	public String evaluate(TemplateInterpreter interpreter, Scope scope) {
		TemplateImmediateExpression evaluated = evaluate(interpreter, scope, emptyList());
		
		return evaluated.getText();
	}

	public String evaluate(TemplateInterpreter interpreter, Scope scope, TemplateVariable... arguments) {
		TemplateImmediateExpression evalutated = evaluate(interpreter, scope, asList(arguments));

		return evalutated.getText();
	}

	public abstract TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments);

	protected List<TemplateVariable> createVariables(List<TemplateVariable> arguments) {
		return parameters.stream()
			.map(parameter -> parameter.from(arguments))
			.filter(Objects::nonNull)
			.collect(toList());
	}

	protected Optional<TemplateVariable> findVariable(String name, List<TemplateVariable> arguments) {
		return arguments.stream()
			.filter(variable -> name.equals(variable.getName()))
			.findFirst();
	}

	@Override
	public String toString() {
		return name + parameters.stream()
			.map(param -> param.toString())
			.collect(joining(", ", "(", ")"));
	}

}
