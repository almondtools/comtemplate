package com.almondtools.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.almondtools.comtemplate.engine.Resolver;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ErrorExpression;
import com.almondtools.comtemplate.engine.expressions.ResolutionErrors;

public class CompoundResolver implements Resolver {

	private List<Resolver> resolvers;

	public CompoundResolver(Resolver... init) {
		this.resolvers = new ArrayList<>(asList(init));
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
		return new ResolutionErrors(errors);
	}

}
