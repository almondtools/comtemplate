package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.almondtools.comtemplate.engine.Resolver;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TestResolver;

public class ExpressionResolutionErrorTest {

	@Test
	public void testGetMessageBase() throws Exception {
		ExpressionResolutionError error = new ExpressionResolutionError(string("base"), "function", asList(string("a"), string("b")), null, null);
		
		assertThat(error.getMessage(),
			containsString("cannot evaluate <'base'.function('a','b')>"));
	}

	@Test
	public void testGetMessageResolver() throws Exception {
		Resolver resolver = new TestResolver();

		ExpressionResolutionError error = new ExpressionResolutionError(string("base"), "function", asList(string("a"), string("b")), null, resolver);

		assertThat(error.getMessage(), containsString("evaluated by <TestResolver>"));
	}

	@Test
	public void testGetMessageScopes() throws Exception {
		Scope scope = mock(Scope.class, RETURNS_DEEP_STUBS);
		when(scope.getDefinition().getName()).thenReturn("child");
		when(scope.getVariables()).thenReturn(asList(var("a", string("a"))));
		when(scope.getParent().getDefinition().getName()).thenReturn("parent");
		when(scope.getParent().getVariables()).thenReturn(asList(var("b", string("b")), var("c", string("c"))));
		when(scope.getParent().getParent()).thenReturn(null);

		ExpressionResolutionError error = new ExpressionResolutionError(string("base"), "function", asList(string("a"), string("b")), scope, null);
		String message = error.getMessage();
		
		assertThat(message, containsString("scope stack:"));
		assertThat(message, containsString("child:[a='a']"));
		assertThat(message, containsString("parent:[b='b', c='c']"));
	}

}
