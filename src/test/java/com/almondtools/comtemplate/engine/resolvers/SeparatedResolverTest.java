package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.expressions.RawText;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;

public class SeparatedResolverTest {

	private SeparatedResolver resolver;

	@Before
	public void before() {
		resolver = new SeparatedResolver();
	}

	@Test
	public void testResolveList() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(new ResolvedListLiteral(string("a"), string("b")), asList(string(":")), scope), equalTo(new RawText("a:b")));
	}

	@Test
	public void testResolveOther() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(string("ab"), asList(string(":")), scope), equalTo(string("ab")));
	}

}
