package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;


public class EqualsResolverTest {

	private EqualsResolver resolver;

	@Before
	public void before() {
		resolver = new EqualsResolver();
	}

	@Test
	public void testEqualTrue() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(string("abc"), asList(string("abc")), scope).as(Boolean.class), is(true));
		assertThat(resolver.resolve(string("a b  c"), asList(string("a b  c")), scope).as(Boolean.class), is(true));
	}

	@Test
	public void testEqualFalse() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(string("abc"), asList(string("xyz")), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(string("abc"), asList(string(" abc ")), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(string("a b  c"), asList(string("a b c")), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(string("a b  c"), asList(string("ab  c")), scope).as(Boolean.class), is(false));
	}

}
