package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.Evaluated;
import com.almondtools.comtemplate.engine.expressions.NativeObject;
import com.almondtools.comtemplate.engine.expressions.RawText;

public class TrimResolverTest {

	private TrimResolver resolver;

	@BeforeEach
	public void before() {
		resolver = new TrimResolver();
	}

	@Test
	public void testResolveStringLiteral() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = string(" \tText\n\r \t");

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text"));
	}

	@Test
	public void testResolveRawText() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new RawText(" \tText\n\r \t");

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text"));
	}

	@Test
	public void testResolveEvaluatedEmpty() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(Collections.emptyList());

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo(""));
	}

	@Test
	public void testResolveEvaluatedSingleElement() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(string(" \tText\n\r \t"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text"));
	}

	@Test
	public void testResolveEvaluatedTwoElements() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(string(" \tText "), string("\nText\n\r \t"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text \nText"));
	}

	@Test
	public void testResolveEvaluatedThreeElements() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(string(" \tText"), string(" \n"), string("Text\n\r \t"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text \nText"));
	}

	@Test
	public void testResolveNativeObjectString() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new NativeObject(" \tText \nText\n\r \t");

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text \nText"));
	}

	@Test
	public void testResolveNativeObjectOther() throws Exception {
		Scope scope = mock(Scope.class);
		Object object = new Object();
		TemplateImmediateExpression base = new NativeObject(object);
		
		assertThat(resolver.resolve(base, emptyList(), scope), equalTo(new NativeObject(object)));
	}
	
	@Test
	public void testResolveOther() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = TRUE;

		assertThat(resolver.resolve(base, emptyList(), scope), equalTo(TRUE));
	}

	@Test
	public void testTrimStringLiteral() throws Exception {
		assertThat(resolver.trim(string(" string "), s -> s.replaceAll("\\s*$", "")).getText(), equalTo(" string"));
	}

	@Test
	public void testTrimRawText() throws Exception {
		assertThat(resolver.trim(new RawText(" string "), s -> s.replaceAll("^\\s*", "")).getText(), equalTo("string "));
	}

	@Test
	public void testTrimNativeString() throws Exception {
		assertThat(resolver.trim(new NativeObject(" string "), s -> s.trim()).getText(), equalTo("string"));
	}

	@Test
	public void testTrimNativeObject() throws Exception {
		assertThat(resolver.trim(new NativeObject(42), s -> s.trim()), equalTo(new NativeObject(42)));
	}

	@Test
	public void testTrimOther() throws Exception {
		assertThat(resolver.trim(FALSE, s -> s.trim()), equalTo(FALSE));
	}

}
