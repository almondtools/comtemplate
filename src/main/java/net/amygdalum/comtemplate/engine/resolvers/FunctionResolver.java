package net.amygdalum.comtemplate.engine.resolvers;

import java.util.List;

import net.amygdalum.comtemplate.engine.Resolver;
import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;

public abstract class FunctionResolver implements Resolver {

	private String name;
	private int arity;

	public FunctionResolver(String name) {
		this(name, 0);
	}

	public FunctionResolver(String name, int arity) {
		this.name = name;
		this.arity = arity;
	}
	
	public String getName() {
		return name;
	}
	
	public int getArity() {
		return arity;
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (name.equals(function) && arguments.size() == arity) {
			return resolve(base, arguments, scope);
		}
		return new ExpressionResolutionError(base, function, arguments, scope, this);
	}

	public abstract TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope);

}
