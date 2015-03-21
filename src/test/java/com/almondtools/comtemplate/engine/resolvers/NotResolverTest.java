package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;

public class NotResolverTest {

	private NotResolver resolver;

	@Before
	public void before() {
		resolver = new NotResolver();
	}

	@Test
	public void testResolveBoolean() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(FALSE, emptyList(), scope), equalTo(TRUE));
	}

	@Test
	public void testResolveOther() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(string("s"), emptyList(), scope), instanceOf(ExpressionResolutionError.class));
	}

}
