package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;


public class ItemResolverTest {

	private ItemResolver resolver;

	@Before
	public void before() {
		resolver = new ItemResolver();
	}

	@Test
	public void testResolveItem() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral(string("s1"), string("s2"));

		assertThat(resolver.resolve(base, asList(integer(0)), scope), equalTo(string("s1")));
		assertThat(resolver.resolve(base, asList(integer(1)), scope), equalTo(string("s2")));
	}

}
