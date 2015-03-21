package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ResolutionErrors;

public class CompoundResolverTest {

	private CompoundResolver resolver;

	@Before
	public void before() throws Exception {
		resolver = new CompoundResolver();
	}

	@Test
	public void testAdd() throws Exception {
		NotResolver notResolver = new NotResolver();

		resolver.add(notResolver);

		assertThat(resolver.getResolvers(), contains(notResolver));
	}

	@Test
	public void testResolveFirst() throws Exception {
		resolver.add(new NotResolver());
		Scope scope = mock(Scope.class);

		TemplateImmediateExpression resolved = resolver.resolve(FALSE, "not", emptyList(), scope);

		assertThat(resolved, equalTo(TRUE));
	}

	@Test
	public void testResolveSecond() throws Exception {
		resolver.add(new TrimResolver());
		resolver.add(new NotResolver());
		Scope scope = mock(Scope.class);

		TemplateImmediateExpression resolved = resolver.resolve(FALSE, "not", emptyList(), scope);

		assertThat(resolved, equalTo(TRUE));
	}

	@Test
	public void testResolveFails() throws Exception {
		resolver.add(new TrimResolver());
		Scope scope = mock(Scope.class);

		TemplateImmediateExpression resolved = resolver.resolve(FALSE, "not", emptyList(), scope);

		assertThat(resolved, instanceOf(ResolutionErrors.class));
	}

}
