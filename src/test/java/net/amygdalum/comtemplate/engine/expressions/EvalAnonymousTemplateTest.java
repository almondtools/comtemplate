package net.amygdalum.comtemplate.engine.expressions;

import static net.amygdalum.comtemplate.engine.expressions.EvalAnonymousTemplate.firstExpression;
import static net.amygdalum.comtemplate.engine.expressions.EvalAnonymousTemplate.lastExpression;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;

public class EvalAnonymousTemplateTest {

	@Test
	public void testGetDefinition() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition);

		assertThat(eval.getDefinition(), sameInstance(definition));
	}

	@Test
	public void testGetExpressionsEmpty() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition);

		assertThat(eval.getExpressions(), empty());
	}

	@Test
	public void testGetExpressions() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition, string("str"));

		assertThat(eval.getExpressions(), contains(string("str")));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition);

		eval.apply(visitor, scope);

		verify(visitor).visitEvalAnonymousTemplate(eval, scope);
	}

	@Test
	public void testFirstExpressionOnEmpty() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition);

		assertThat(firstExpression(eval.getExpressions(), StringLiteral.class), nullValue());
	}

	@Test
	public void testFirstExpressionOnMismatch() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition, string("mismatch"), integer(2));

		assertThat(firstExpression(eval.getExpressions(), IntegerLiteral.class), nullValue());
	}

	@Test
	public void testFirstExpressionOnMatch() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition, string("match"), integer(2));

		assertThat(firstExpression(eval.getExpressions(), StringLiteral.class), equalTo(string("match")));
	}

	@Test
	public void testLastExpressionEmpty() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition);

		assertThat(lastExpression(eval.getExpressions(), StringLiteral.class), nullValue());
	}

	@Test
	public void testLastExpressionOnMismatch() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition, integer(2), string("mismatch"));

		assertThat(lastExpression(eval.getExpressions(), IntegerLiteral.class), nullValue());
	}

	@Test
	public void testLastExpressionOnMatch() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition, integer(2), string("match"));

		assertThat(lastExpression(eval.getExpressions(), StringLiteral.class), equalTo(string("match")));
	}

	@Test
	public void testStripEnclosingNewLinesOnEmpty() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition);

		assertThat(eval.getExpressions(), empty());
	}

	@Test
	public void testStripEnclosingNewLinesNoWhitespace() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition, new RawText("begin"), integer(2), new RawText("end"));

		assertThat(eval.getExpressions(), contains(new RawText("begin"), integer(2), new RawText("end")));
	}

	@Test
	public void testStripEnclosingNewLinesOnlyNewlines() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition, new RawText("\n\nbegin"), integer(2), new RawText("end\n\n"));

		assertThat(eval.getExpressions(), contains(new RawText("\nbegin"), integer(2), new RawText("end\n")));
	}

	@Test
	public void testStripEnclosingNewLinesWhitespaceAndNewlines() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition, new RawText("\t\nbegin"), integer(2), new RawText("end\n  "));

		assertThat(eval.getExpressions(), contains(new RawText("begin"), integer(2), new RawText("end")));
	}

	@Test
	public void testStripEnclosingNewLinesLinefeedNewlines() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition, new RawText("\r\nbegin"), integer(2), new RawText("end\r"));

		assertThat(eval.getExpressions(), contains(new RawText("begin"), integer(2), new RawText("end")));
	}

	@Test
	public void testStripEnclosingNewLinesMismatchedBorders() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalAnonymousTemplate eval = new EvalAnonymousTemplate(definition, string("\nbegin"), integer(2), string("end\n"));
		
		assertThat(eval.getExpressions(), contains(string("\nbegin"), integer(2), string("end\n")));
	}
	
	@Test
	public void testToString() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalAnonymousTemplate(definition, string("one:"), integer(1)).toString(), equalTo("{'one:',1}"));
	}

}
