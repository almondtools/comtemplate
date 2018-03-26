package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;

public class MapResolverTest {

	private MapResolver resolver;

	@BeforeEach
	public void before() {
		resolver = new MapResolver();
	}

	@Test
	public void testResolveValues() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedMapLiteral base = new ResolvedMapLiteral(var("k1",string("s1")), var("k2",string("s2")));

		assertThat(resolver.resolveTyped(base, "values", emptyList(), scope), equalTo(new ResolvedListLiteral(string("s1"), string("s2"))));
	}

	@Test
	public void testResolveValuesEmptyMap() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedMapLiteral base = new ResolvedMapLiteral();

		assertThat(resolver.resolveTyped(base, "values", emptyList(), scope), equalTo(new ResolvedListLiteral()));
	}

	@Test
	public void testResolveKeys() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedMapLiteral base = new ResolvedMapLiteral(var("k1",string("s1")), var("k2",string("s2")));

		assertThat(resolver.resolveTyped(base, "keys", emptyList(), scope), equalTo(new ResolvedListLiteral(string("k1"), string("k2"))));
	}

	@Test
	public void testResolveKeysEmptyMap() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedMapLiteral base = new ResolvedMapLiteral();

		assertThat(resolver.resolveTyped(base, "keys", emptyList(), scope), equalTo(new ResolvedListLiteral()));
	}

	@Test
	public void testResolveSize() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedMapLiteral base = new ResolvedMapLiteral(var("k1",string("s1")), var("k2",string("s2")));

		assertThat(resolver.resolveTyped(base, "size", emptyList(), scope), equalTo(integer(2)));
	}

	@Test
	public void testResolveSizeEmptyList() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedMapLiteral base = new ResolvedMapLiteral();

		assertThat(resolver.resolveTyped(base, "size", emptyList(), scope), equalTo(integer(0)));
	}

	@Test
	public void testResolveEntries() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedMapLiteral base = new ResolvedMapLiteral(var("k1",string("s1")), var("k2",string("s2")));

		assertThat(resolver.resolveTyped(base, "entries", emptyList(), scope), equalTo(new ResolvedListLiteral(new ResolvedMapLiteral(var("k1",string("s1"))), new ResolvedMapLiteral(var("k2",string("s2"))))));
	}

	@Test
	public void testResolveEntriesEmptyMap() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedMapLiteral base = new ResolvedMapLiteral();

		assertThat(resolver.resolveTyped(base, "entries", emptyList(), scope), equalTo(new ResolvedListLiteral()));
	}

	@Test
	public void testResolveElse() throws Exception {
		Scope scope = mock(Scope.class);
		ResolvedMapLiteral base = new ResolvedMapLiteral();

		assertThat(resolver.resolveTyped(base, "else", emptyList(), scope), instanceOf(ExpressionResolutionError.class));
	}

}
