package com.almondtools.comtemplate.engine;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import com.almondtools.comtemplate.engine.resolvers.BasicResolver;
import com.almondtools.comtemplate.engine.resolvers.BeanDynamicResolver;
import com.almondtools.comtemplate.engine.resolvers.CompoundResolver;
import com.almondtools.comtemplate.engine.resolvers.EqualsResolver;
import com.almondtools.comtemplate.engine.resolvers.ItemResolver;
import com.almondtools.comtemplate.engine.resolvers.ListResolver;
import com.almondtools.comtemplate.engine.resolvers.MapDynamicResolver;
import com.almondtools.comtemplate.engine.resolvers.MapResolver;
import com.almondtools.comtemplate.engine.resolvers.NotResolver;
import com.almondtools.comtemplate.engine.resolvers.SeparatedResolver;

public class ResolverRegistry {

	private Map<Class<? extends TemplateImmediateExpression>, Resolver> resolvers;

	public ResolverRegistry() {
		resolvers = new HashMap<>();
	}

	private void register(Class<? extends TemplateImmediateExpression> clazz, Resolver functionResolver) {
		Resolver resolver = resolvers.get(clazz);
		if (resolver == null) {
			resolvers.put(clazz, functionResolver);
		} else if (resolver instanceof CompoundResolver) {
			((CompoundResolver) resolver).add(functionResolver);
		} else {
			resolvers.put(clazz, new CompoundResolver(clazz, resolver, functionResolver));
		}
	}

	public void register(Resolver functionResolver) {
		for (Class<? extends TemplateImmediateExpression> clazz : functionResolver.getResolvedClasses()) {
			register(clazz, functionResolver);
		}
	}

	@SuppressWarnings("unchecked")
	public Resolver getResolverFor(TemplateImmediateExpression expression) {
		Class<? extends TemplateImmediateExpression>  clazz = expression.getClass();
		Set<Resolver> foundresolvers = new LinkedHashSet<>();
		while (clazz != null) {
			Resolver resolver = resolvers.get(clazz);
			if (resolver != null) {
				foundresolvers.add(resolver);
			}
			for (Class<?> interfaze : clazz.getInterfaces()) {
				resolver = resolvers.get(interfaze);
				if (resolver != null) {
					foundresolvers.add(resolver);
				}
			}
		    Class<?> superclass = clazz.getSuperclass();
			clazz = TemplateImmediateExpression.class.isAssignableFrom(superclass) ? (Class<? extends TemplateImmediateExpression>) superclass : null;
		}
		if (foundresolvers.isEmpty()) {
			return Resolver.NULL;
		} else if (foundresolvers.size() == 1) {
			return foundresolvers.iterator().next();
		} else {
			return new CompoundResolver(clazz, foundresolvers.toArray(new Resolver[0]));
		}
	}

	public static ResolverRegistry defaultRegistry() {
		ResolverRegistry registry = new ResolverRegistry();

		registry.register(new MapDynamicResolver());
		registry.register(new BeanDynamicResolver());

		registry.register(new BasicResolver());
		registry.register(new ListResolver());
		registry.register(new MapResolver());
		registry.register(new NotResolver());
		
		registry.register(new SeparatedResolver());
		registry.register(new ItemResolver());

		registry.register(new EqualsResolver());

		ServiceLoader<Resolver> resolverService = ServiceLoader.load(Resolver.class);
		for (Resolver resolver : resolverService) {
			registry.register(resolver);
		}
		return registry;
	}

}
