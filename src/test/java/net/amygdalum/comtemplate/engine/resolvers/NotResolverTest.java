package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.resolvers.NotResolver;

public class NotResolverTest {

	private NotResolver resolver;

	@BeforeEach
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
