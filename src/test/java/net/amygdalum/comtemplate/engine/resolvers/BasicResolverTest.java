package net.amygdalum.comtemplate.engine.resolvers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.resolvers.BasicResolver;
import net.amygdalum.comtemplate.engine.resolvers.CompressResolver;
import net.amygdalum.comtemplate.engine.resolvers.EmptyResolver;
import net.amygdalum.comtemplate.engine.resolvers.IndentResolver;
import net.amygdalum.comtemplate.engine.resolvers.TrimResolver;

public class BasicResolverTest {

	private BasicResolver resolvers;

	@BeforeEach
	public void before() throws Exception {
		resolvers = new BasicResolver();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDefaultResolvers() throws Exception {
		assertThat(resolvers.getResolvers(), containsInAnyOrder(
			instanceOf(EmptyResolver.class),
			instanceOf(TrimResolver.class),
			instanceOf(IndentResolver.class),
			instanceOf(CompressResolver.class)));
	}

}
