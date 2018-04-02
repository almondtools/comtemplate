package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionErrors;
import net.amygdalum.comtemplate.engine.resolvers.CompoundResolver;
import net.amygdalum.comtemplate.engine.resolvers.NotResolver;
import net.amygdalum.comtemplate.engine.resolvers.TrimResolver;

public class CompoundResolverTest {

	private CompoundResolver resolver;

	@BeforeEach
	public void before() throws Exception {
		resolver = new CompoundResolver(TemplateImmediateExpression.class);
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

		assertThat(resolved, instanceOf(ExpressionResolutionErrors.class));
	}

}
