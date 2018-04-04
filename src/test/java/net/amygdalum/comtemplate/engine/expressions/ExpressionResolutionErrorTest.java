package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Resolver;
import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.TestResolver;

public class ExpressionResolutionErrorTest {

	@Test
	public void testGetMessageBase() throws Exception {
		ExpressionResolutionError error = new ExpressionResolutionError(string("base"), "function", asList(string("a"), string("b")), null, null);
		
		assertThat(error.getMessage(),
			containsString("cannot evaluate <'base'.function('a','b')>"));
	}

	@Test
	public void testGetMessageResolver() throws Exception {
		Resolver resolver = new TestResolver(TemplateImmediateExpression.class);

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
