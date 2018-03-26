package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static net.amygdalum.xrayinterface.XRayInterface.xray;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.expressions.StringLiteral;
import com.almondtools.comtemplate.engine.resolvers.BasicResolver;
import com.almondtools.comtemplate.engine.resolvers.BeanDynamicResolver;
import com.almondtools.comtemplate.engine.resolvers.CompoundResolver;
import com.almondtools.comtemplate.engine.resolvers.CompressResolver;
import com.almondtools.comtemplate.engine.resolvers.EmptyResolver;
import com.almondtools.comtemplate.engine.resolvers.EqualsResolver;
import com.almondtools.comtemplate.engine.resolvers.IndentResolver;
import com.almondtools.comtemplate.engine.resolvers.ItemResolver;
import com.almondtools.comtemplate.engine.resolvers.ListResolver;
import com.almondtools.comtemplate.engine.resolvers.MapDynamicResolver;
import com.almondtools.comtemplate.engine.resolvers.MapResolver;
import com.almondtools.comtemplate.engine.resolvers.NotResolver;
import com.almondtools.comtemplate.engine.resolvers.SeparatedResolver;
import com.almondtools.comtemplate.engine.resolvers.TrimResolver;


public class ResolverRegistryTest {

	@Test
	public void testRegisterResolveExactMatch() throws Exception {
		ResolverRegistry registry = new ResolverRegistry();
		Resolver resolver = new TestResolver(StringLiteral.class);
		registry.register(resolver);
		assertThat(registry.getResolverFor(string("abc")), sameInstance(resolver));
	}

	@Test
	public void testRegisterResolveNoMatch() throws Exception {
		ResolverRegistry registry = new ResolverRegistry();
		assertThat(registry.getResolverFor(string("abc")), sameInstance(Resolver.NULL));
	}
	
	@Test
	public void testRegisterResolveGeneralizedMatch() throws Exception {
		ResolverRegistry registry = new ResolverRegistry();
		Resolver resolver = new TestResolver(TemplateImmediateExpression.class);
		registry.register(resolver);
		assertThat(registry.getResolverFor(string("abc")), sameInstance(resolver));
	}
	
	@Test
	public void testRegisterResolveMultipleMatch() throws Exception {
		ResolverRegistry registry = new ResolverRegistry();
		registry.register(new TestResolver(TemplateImmediateExpression.class));
		registry.register(new TestResolver(StringLiteral.class));
		assertThat(registry.getResolverFor(string("abc")), instanceOf(CompoundResolver.class));
	}
	
	@Test
	public void testRegisterResolveTwoRegisters() throws Exception {
		ResolverRegistry registry = new ResolverRegistry();
		registry.register(new TestResolver(StringLiteral.class));
		registry.register(new TestResolver(StringLiteral.class));
		assertThat(registry.getResolverFor(string("abc")), instanceOf(CompoundResolver.class));
		CompoundResolver resolver = (CompoundResolver) registry.getResolverFor(string("abc"));
		assertThat(resolver.getResolvers(), hasSize(2));
	}
	
	@Test
	public void testRegisterResolveThreeRegisters() throws Exception {
		ResolverRegistry registry = new ResolverRegistry();
		registry.register(new TestResolver(StringLiteral.class));
		registry.register(new TestResolver(StringLiteral.class));
		registry.register(new TestResolver(StringLiteral.class));
		assertThat(registry.getResolverFor(string("abc")), instanceOf(CompoundResolver.class));
		CompoundResolver resolver = (CompoundResolver) registry.getResolverFor(string("abc"));
		assertThat(resolver.getResolvers(), hasSize(3));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testDefaultRegistry() throws Exception {
		ResolverRegistry registry = defaultRegistry();
		Map<String, Resolver> resolverMap = xray(registry).to(OpenRegistry.class).getResolvers();
		Set<Resolver> resolvers = new HashSet<>();
		for (Resolver resolver : resolverMap.values()) {
			resolvers.add(resolver);
			if (resolver instanceof CompoundResolver) {
				resolvers.addAll(((CompoundResolver) resolver).getResolvers());
			} 
		}
		assertThat(resolvers, containsInAnyOrder(
			Matchers.instanceOf(MapDynamicResolver.class),
			instanceOf(BeanDynamicResolver.class),
			instanceOf(BasicResolver.class),
			instanceOf(CompoundResolver.class),
			instanceOf(ListResolver.class),
			instanceOf(CompoundResolver.class),
			instanceOf(MapResolver.class),
			instanceOf(NotResolver.class),
			instanceOf(SeparatedResolver.class),
			instanceOf(ItemResolver.class),
			instanceOf(EmptyResolver.class),
			instanceOf(TrimResolver.class),
			instanceOf(IndentResolver.class),
			instanceOf(CompressResolver.class),
			instanceOf(EqualsResolver.class)
			));
	}
	
	interface OpenRegistry {
		Map<String, Resolver> getResolvers();
	}

}
