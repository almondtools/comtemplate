package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;


public class EvaluatesToResolverTest {

	private EvaluatesToResolver resolver;

	@Before
	public void before() {
		resolver = new EvaluatesToResolver();
	}

	@Test
	public void testEvaluatesTrue() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(string("abc"), asList(string("abc")), scope).as(Boolean.class), is(true));
		assertThat(resolver.resolve(string("abc"), asList(string(" abc")), scope).as(Boolean.class), is(true));
		assertThat(resolver.resolve(string("abc"), asList(string("abc ")), scope).as(Boolean.class), is(true));
		assertThat(resolver.resolve(string(" abc"), asList(string("abc")), scope).as(Boolean.class), is(true));
		assertThat(resolver.resolve(string("abc "), asList(string("abc")), scope).as(Boolean.class), is(true));
		assertThat(resolver.resolve(string("a b  c"), asList(string("a b c")), scope).as(Boolean.class), is(true));
		assertThat(resolver.resolve(string(" a \nb\t c\r"), asList(string("a b c")), scope).as(Boolean.class), is(true));
	}

	@Test
	public void testEvaluatesFalse() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(string("abc"), asList(string("xyz")), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(string(" a \nb\t c\r"), asList(string("abc")), scope).as(Boolean.class), is(false));
	}

}
