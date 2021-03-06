package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;
import net.amygdalum.comtemplate.engine.expressions.TestError;

public class ListResolverTest {

	private ListResolver resolver;

	@BeforeEach
	public void before() {
		resolver = new ListResolver();
	}

	@Test
	public void testResolveItemEmptyList() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral();

		assertThat(resolver.resolveTyped(base, "item", asList(integer(0)), scope), instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveItemOutOfBounds() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral(string("s"));

		assertThat(resolver.resolveTyped(base, "item", asList(integer(-1)), scope), instanceOf(ExpressionResolutionError.class));
		assertThat(resolver.resolveTyped(base, "item", asList(integer(1)), scope), instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveSize() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral(string("s1"), string("s2"));

		assertThat(resolver.resolveTyped(base, "size", emptyList(), scope), equalTo(integer(2)));
	}

	@Test
	public void testResolveSizeEmptyList() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral();

		assertThat(resolver.resolveTyped(base, "size", emptyList(), scope), equalTo(integer(0)));
	}

	@Test
	public void testResolveFirst() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral(string("s1"), string("s2"));

		assertThat(resolver.resolveTyped(base, "first", emptyList(), scope), equalTo(string("s1")));
	}

	@Test
	public void testResolveFirstEmptyList() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral();

		assertThat(resolver.resolveTyped(base, "first", emptyList(), scope), instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveLast() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral(string("s1"), string("s2"));

		assertThat(resolver.resolveTyped(base, "last", emptyList(), scope), equalTo(string("s2")));
	}

	@Test
	public void testResolveLastEmptyList() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral();

		assertThat(resolver.resolveTyped(base, "last", emptyList(), scope), instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveRest() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral(string("s1"), string("s2"), string("s3"));

		assertThat(resolver.resolveTyped(base, "rest", emptyList(), scope), equalTo(new ResolvedListLiteral(string("s2"), string("s3"))));
	}

	@Test
	public void testResolveRestEmptyList() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral();

		assertThat(resolver.resolveTyped(base, "rest", emptyList(), scope), instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveTrunc() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral(string("s1"), string("s2"), string("s3"));

		assertThat(resolver.resolveTyped(base, "trunc", emptyList(), scope), equalTo(new ResolvedListLiteral(string("s1"), string("s2"))));
	}

	@Test
	public void testResolveTruncEmptyList() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral();

		assertThat(resolver.resolveTyped(base, "trunc", emptyList(), scope), instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveStrip() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral(string("s1"), new TestError(), string("s2"), string("s3"));

		assertThat(resolver.resolveTyped(base, "strip", emptyList(), scope), equalTo(new ResolvedListLiteral(string("s1"), string("s2"), string("s3"))));
	}

	@Test
	public void testResolveStripEmptyList() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral();

		assertThat(resolver.resolveTyped(base, "strip", emptyList(), scope), equalTo(new ResolvedListLiteral()));
	}

	@Test
	public void testResolveStripErrorList() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral(new TestError(), new TestError());

		assertThat(resolver.resolveTyped(base, "strip", emptyList(), scope), equalTo(new ResolvedListLiteral()));
	}

	@Test
	public void testElse() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedListLiteral base = new ResolvedListLiteral();

		assertThat(resolver.resolveTyped(base, "else", emptyList(), scope), instanceOf(ExpressionResolutionError.class));
	}

}
