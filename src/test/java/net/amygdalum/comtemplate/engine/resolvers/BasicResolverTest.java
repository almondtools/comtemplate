package net.amygdalum.comtemplate.engine.resolvers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BasicResolverTest {

	private BasicResolver resolvers;

	@BeforeEach
	public void before() throws Exception {
		resolvers = new BasicResolver();
	}

	@Test
	public void testDefaultResolvers() throws Exception {
		assertThat(resolvers.getResolvers(), containsInAnyOrder(
			instanceOf(EmptyResolver.class),
			instanceOf(TrimResolver.class),
			instanceOf(IndentResolver.class),
			instanceOf(CompressResolver.class)));
	}

}
