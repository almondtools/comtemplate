package com.almondtools.comtemplate.engine;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.almondtools.comtemplate.engine.expressions.BooleanLiteral;
import com.almondtools.comtemplate.engine.expressions.NativeObject;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;
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

	public void register(Class<? extends TemplateImmediateExpression> clazz, Resolver functionResolver) {
		Resolver resolver = resolvers.get(clazz);
		if (resolver == null) {
			resolvers.put(clazz, functionResolver);
		} else if (resolver instanceof CompoundResolver) {
			((CompoundResolver) resolver).add(functionResolver);
		} else {
			resolvers.put(clazz, new CompoundResolver(resolver, functionResolver));
		}
	}

	public Resolver getResolverFor(TemplateImmediateExpression expression) {
		Class<?> clazz = expression.getClass();
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
			clazz = clazz.getSuperclass();
		}
		if (foundresolvers.isEmpty()) {
			return Resolver.NULL;
		} else if (foundresolvers.size() == 1) {
			return foundresolvers.iterator().next();
		} else {
			return new CompoundResolver(foundresolvers.toArray(new Resolver[0]));
		}
	}

	public static ResolverRegistry defaultRegistry() {
		ResolverRegistry registry = new ResolverRegistry();

		registry.register(ResolvedMapLiteral.class, new MapDynamicResolver());
		registry.register(NativeObject.class, new BeanDynamicResolver());

		registry.register(TemplateImmediateExpression.class, new BasicResolver());
		registry.register(ResolvedListLiteral.class, new ListResolver());
		registry.register(ResolvedMapLiteral.class, new MapResolver());
		registry.register(BooleanLiteral.class, new NotResolver());
		
		registry.register(ResolvedListLiteral.class, new SeparatedResolver());
		registry.register(ResolvedListLiteral.class, new ItemResolver());

		registry.register(TemplateImmediateExpression.class, new EqualsResolver());

		return registry;
	}

}
