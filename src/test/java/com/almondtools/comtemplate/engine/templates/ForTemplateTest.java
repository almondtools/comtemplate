package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.ListLiteral.list;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.almondtools.comtemplate.engine.ArgumentRequiredException;
import com.almondtools.comtemplate.engine.expressions.EvalAnonymousTemplate;
import com.almondtools.comtemplate.engine.expressions.EvalContextVar;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.StringLiteral;

public class ForTemplateTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testEvaluateEmptyList() throws Exception {
		ForTemplate template = new ForTemplate();
		ResolvedListLiteral list = new ResolvedListLiteral(emptyList());
		StringLiteral body = string("|");

		String result = template.evaluate(var("var", list), var("do", body));

		assertThat(result, equalTo(""));
	}

	@Test
	public void testEvaluateSingleElementList() throws Exception {
		ForTemplate template = new ForTemplate();
		ResolvedListLiteral list = new ResolvedListLiteral(string("e"));
		StringLiteral body = string("|");

		String result = template.evaluate(var("var", list), var("do", body));

		assertThat(result, equalTo("|"));
	}

	@Test
	public void testEvaluateMultiElementList() throws Exception {
		ForTemplate template = new ForTemplate();
		ResolvedListLiteral list = new ResolvedListLiteral(string("e1"), string("e2"));
		StringLiteral body = string("|");

		String result = template.evaluate(var("var", list), var("do", body));

		assertThat(result, equalTo("||"));
	}

	@Test
	public void testEvaluateSingleElementListWithVariable() throws Exception {
		ForTemplate template = new ForTemplate();
		ResolvedListLiteral list = new ResolvedListLiteral(string("e"));
		EvalAnonymousTemplate body = new EvalAnonymousTemplate(template, new EvalContextVar("ivar"), string("|"));

		String result = template.evaluate(var("var", list), var("do", body));

		assertThat(result, equalTo("e|"));
	}

	@Test
	public void testEvaluateMultiElementListWithVariable() throws Exception {
		ForTemplate template = new ForTemplate();
		ResolvedListLiteral list = new ResolvedListLiteral(string("e1"), string("e2"));
		EvalAnonymousTemplate body = new EvalAnonymousTemplate(template, new EvalContextVar("ivar"), string("|"));

		String result = template.evaluate(var("var", list), var("do", body));

		assertThat(result, equalTo("e1|e2|"));
	}

	@Test
	public void testEvaluateVarIsNotList() throws Exception {
		ForTemplate template = new ForTemplate();
		EvalContextVar body = new EvalContextVar("ivar");
		
		String result = template.evaluate(var("var", string("['a','b']")), var("do", body));

		assertThat(result, equalTo(""));
	}

	@Test
	public void testEvaluateVarIsNotDefined() throws Exception {
		ForTemplate template = new ForTemplate();
		EvalContextVar body = new EvalContextVar("ivar");
		thrown.expect(ArgumentRequiredException.class);
		
		String result = template.evaluate(var("do", body));
		
		assertThat(result, equalTo(""));
	}
	
	@Test
	public void testEvaluateDoIsNotList() throws Exception {
		ForTemplate template = new ForTemplate();
		thrown.expect(ArgumentRequiredException.class);
		
		String result = template.evaluate(var("var", list(string("a"))));

		assertThat(result, equalTo(""));
	}

}
