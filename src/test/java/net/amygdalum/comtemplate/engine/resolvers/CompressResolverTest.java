package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.Evaluated;
import net.amygdalum.comtemplate.engine.expressions.NativeObject;
import net.amygdalum.comtemplate.engine.expressions.RawText;

public class CompressResolverTest {

	private CompressResolver resolver;

	@BeforeEach
	public void before() {
		resolver = new CompressResolver();
	}

	@Test
	public void testResolveStringLiteral() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = string(" \tMy\t\r\nText\n\r \t");

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("My Text"));
	}

	@Test
	public void testResolveRawText() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new RawText(" \tMy\t\r\nText\n\r \t");

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("My Text"));
	}

	@Test
	public void testResolveEvaluatedEmpty() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated();

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo(""));
	}

	@Test
	public void testResolveEvaluatedSingleElement() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(string(" \tMy\t\r\nText\n\r \t"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("My Text"));
	}

	@Test
	public void testResolveEvaluatedMultipleElements() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(string(" \tText "), string("Text\n\r \t"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text Text"));
	}

	@Test
	public void testResolveEvaluatedMultipleElementsWithInnerWhitespace() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(string(" \tText "), string("\nText\n\r \t"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text Text"));
	}

	@Test
	public void testResolveEvaluatedWithLeadingInnerWhitespace() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(string("Text("), integer(2), string(" )"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text(2 )"));
	}

	@Test
	public void testResolveEvaluatedWithTrailingInnerWhitespace() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(string("Text( "), integer(2), string(")"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text( 2)"));
	}

	@Test
	public void testResolveEvaluatedWithNoWhitespace() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(string("Text("), integer(2), string(")"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text(2)"));
	}

	@Test
	public void testResolveNativeString() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new NativeObject(" \tText \tText\n");

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Text Text"));
	}

	@Test
	public void testResolveNativeObject() throws Exception {
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

}
