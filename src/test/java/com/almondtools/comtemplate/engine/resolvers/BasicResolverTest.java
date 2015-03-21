package com.almondtools.comtemplate.engine.resolvers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BasicResolverTest {

	private BasicResolver resolvers;

	@Before
	public void before() throws Exception {
		resolvers = new BasicResolver();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDefaultResolvers() throws Exception {
		assertThat(resolvers.getResolvers(), containsInAnyOrder(
			instanceOf(EmptyResolver.class),
			instanceOf(TrimResolver.class),
			instanceOf(CompressResolver.class)));
	}

}
