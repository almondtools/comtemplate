package com.almondtools.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.almondtools.comtemplate.engine.Resolver;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ErrorExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionErrors;

public class CompoundResolver implements Resolver {

	private Class<? extends TemplateImmediateExpression> clazz;
	private List<Resolver> resolvers;

	public CompoundResolver(Class<? extends TemplateImmediateExpression> clazz, Resolver... init) {
		this.clazz = clazz;
		this.resolvers = flatten(init);
	}

	private List<Resolver> flatten(Resolver[] resolvers) {
		Queue<Resolver> todo = new LinkedList<>(asList(resolvers));
		List<Resolver> allResolvers = new ArrayList<>();
		while (!todo.isEmpty()) {
			Resolver current = todo.remove();
			if (current instanceof CompoundResolver) {
				todo.addAll(((CompoundResolver) current).resolvers);
			} else {
				allResolvers.add(current);
			}
		}
		return allResolvers;
	}

	public void add(Resolver resolver) {
		resolvers.add(resolver);
	}
	
	public List<Resolver> getResolvers() {
		return resolvers;
	}
	
	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
		List<ErrorExpression> errors = new ArrayList<ErrorExpression>();
		for (Resolver resolver : resolvers) {
			TemplateImmediateExpression resolved = resolver.resolve(base, function, arguments, scope);
			if (resolved instanceof ErrorExpression) {
				errors.add((ErrorExpression) resolved);
				continue;
			} else {
				return resolved;
			}
		}
		return new ExpressionResolutionErrors(base, function, arguments, scope, this, errors);
	}
	
	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(clazz);
	}

}
