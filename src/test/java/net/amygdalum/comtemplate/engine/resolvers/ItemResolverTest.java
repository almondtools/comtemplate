package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.BooleanLiteral;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;
import net.amygdalum.comtemplate.engine.resolvers.ItemResolver;


public class ItemResolverTest {

	private ItemResolver resolver;

	@BeforeEach
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

	@Test
	public void testResolveItemOnEmptyList() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral();

		assertThat(resolver.resolve(base, asList(integer(0)), scope), instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveItemOnNullInList() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral((TemplateImmediateExpression) null);

		assertThat(resolver.resolve(base, asList(integer(0)), scope), instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveItemOnOther() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(BooleanLiteral.TRUE, asList(integer(0)), scope), instanceOf(ExpressionResolutionError.class));
	}

}
