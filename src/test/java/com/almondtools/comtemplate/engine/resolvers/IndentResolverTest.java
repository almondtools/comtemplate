package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.BooleanLiteral;
import com.almondtools.comtemplate.engine.expressions.Evaluated;
import com.almondtools.comtemplate.engine.expressions.RawText;


public class IndentResolverTest {

	private IndentResolver resolver;

	@Before
	public void before() {
		resolver = new IndentResolver();
	}

	@Test
	public void testOnlyRawTextAlreadyIndented() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new RawText("Line1\nLine2");

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\nLine2"));
	}

	@Test
	public void testOnlyRawTextSameLevel() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new RawText(" Line1\n Line2");

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\nLine2"));
	}

	@Test
	public void testOnlyRawTextDifferentLevels() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new RawText(" Line1\n       IndentedLine\n Line2");

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\n      IndentedLine\nLine2"));
	}

	@Test
	public void testOnlyRawTextDifferentInhomogenousLevels() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new RawText(" Line1\n   IndentedLine1\n    IndentedLine2\n Line2");

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\n  IndentedLine1\n   IndentedLine2\nLine2"));
	}

	@Test
	public void testInterruptedRawTextSameLevel() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(new RawText(" Line1"), new RawText("\n Line2"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\nLine2"));
	}

	@Test
	public void testInterruptedRawTextSameLevelInline() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(new RawText(" Line1"), new RawText(" Line2"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1 Line2"));
	}

	@Test
	public void testInterruptedRawTextDifferentLevels() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(new RawText(" Line1"), new RawText("\n  Line2"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\n Line2"));
	}

	@Test
	public void testMixedTextSameLevel() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(new RawText(" Line1\n "), integer(2), new RawText("\n Line2"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\n2\nLine2"));
	}

	@Test
	public void testMixedTextSameLevelInline() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(new RawText(" Line1\n "), integer(2), new RawText(" Line2"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\n2 Line2"));
	}

	@Test
	public void testMixedTextDifferentLevels() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression base = new Evaluated(new RawText(" Line1\n  "), integer(2), new RawText("\n Line2"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\n 2\nLine2"));
	}

	@Test
	public void testNestedText() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression nested = new Evaluated(new RawText(" Line1.1\n   "), integer(2), new RawText("\n Line2.2"));
		TemplateImmediateExpression base = new Evaluated(new RawText(" Line1\n   "), nested, new RawText("\n Line2"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\n  Line1.1\n    2\n  Line2.2\nLine2"));
	}
		
	@Test
	public void testNestedTextInline() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression nested = new Evaluated(new RawText(" a "), integer(2), new RawText(" c"));
		TemplateImmediateExpression base = new Evaluated(new RawText(" Line1\n Line1,5   "), nested, new RawText("\n Line2"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\nLine1,5    a 2 c\nLine2"));
	}
	
	@Test
	public void testDeepNestedTextDifferentLevels() throws Exception {
		Scope scope = mock(Scope.class);
		TemplateImmediateExpression deepNested = new Evaluated(new RawText(" Text\n     "), integer(2), new RawText("\n "));
		TemplateImmediateExpression nested = new Evaluated(new RawText(" Line1.1\n   "), deepNested, new RawText("\n Line2.2"));
		TemplateImmediateExpression base = new Evaluated(new RawText(" Line1\n  "), nested, new RawText("\n  Line2"));

		assertThat(resolver.resolve(base, emptyList(), scope).getText(), equalTo("Line1\n Line1.1\n   Text\n       2\n   \n Line2.2\n Line2"));
	}

	@Test
	public void testResolveItemOnOther() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(BooleanLiteral.TRUE, emptyList(), scope), equalTo(BooleanLiteral.TRUE));
	}

}
