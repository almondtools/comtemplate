package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;

public class EvalFunction implements TemplateExpression {

	private TemplateExpression base;
	private String function;
	private List<TemplateExpression> arguments;

	public EvalFunction(TemplateExpression base, String function, List<TemplateExpression> arguments) {
		this.base = base;
		this.function = function;
		this.arguments = arguments;
	}

	public EvalFunction(TemplateExpression base, String function, TemplateExpression... arguments) {
		this(base, function, asList(arguments));
	}
	
	public TemplateExpression getBase() {
		return base;
	}

	public String getFunction() {
		return function;
	}
	
	public List<TemplateExpression> getArguments() {
		return arguments;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitEvalFunction(this, scope);
	}
	
	@Override
	public String toString() {
		return base.toString() + '.' + function + arguments.stream()
			.map(argument -> argument.toString())
			.collect(joining(",", "(", ")"));
	}

}
