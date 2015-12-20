package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.expressions.BooleanLiteral;
import com.almondtools.comtemplate.engine.expressions.Evaluated;
import com.almondtools.comtemplate.engine.expressions.NativeObject;
import com.almondtools.comtemplate.engine.expressions.RawText;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;

public class EmptyResolverTest {

	private EmptyResolver resolver;

	@Before
	public void before() {
		resolver = new EmptyResolver();
	}

	@Test
	public void testResolveStringLiteral() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(string("str"), emptyList(), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(string(""), emptyList(), scope).as(Boolean.class), is(true));
	}

	@Test
	public void testResolveRawText() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(new RawText("str"), emptyList(), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(new RawText(""), emptyList(), scope).as(Boolean.class), is(true));
	}

	@Test
	public void testResolveEvaluated() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(new Evaluated(string("str")), emptyList(), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(new Evaluated(string("")), emptyList(), scope).as(Boolean.class), is(true));
		assertThat(resolver.resolve(new Evaluated(string(""), string("str")), emptyList(), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(new Evaluated(string("str"), string("")), emptyList(), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(new Evaluated(), emptyList(), scope).as(Boolean.class), is(true));
	}

	@Test
	public void testResolveNative() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(new NativeObject("str"), emptyList(), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(new NativeObject(""), emptyList(), scope).as(Boolean.class), is(true));
		assertThat(resolver.resolve(new NativeObject(asList("")), emptyList(), scope).as(Boolean.class), is(true));
		assertThat(resolver.resolve(new NativeObject(asList("", "str")), emptyList(), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(new NativeObject(asList("str","")), emptyList(), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(new NativeObject(emptyList()), emptyList(), scope).as(Boolean.class), is(true));
		assertThat(resolver.resolve(new NativeObject(new Object()), emptyList(), scope).as(Boolean.class), is(false));
	}

	@Test
	public void testResolveListLiteral() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(new ResolvedListLiteral(string("str")), emptyList(), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(new ResolvedListLiteral(), emptyList(), scope).as(Boolean.class), is(true));
	}

	@Test
	public void testResolvemapLiteral() throws Exception {
		Scope scope = mock(Scope.class);
		
		assertThat(resolver.resolve(new ResolvedMapLiteral(var("str", string("str"))), emptyList(), scope).as(Boolean.class), is(false));
		assertThat(resolver.resolve(new ResolvedMapLiteral(), emptyList(), scope).as(Boolean.class), is(true));
	}
	
	@Test
	public void testResolveOther() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(BooleanLiteral.TRUE, emptyList(), scope).as(Boolean.class), is(false));
	}

}
