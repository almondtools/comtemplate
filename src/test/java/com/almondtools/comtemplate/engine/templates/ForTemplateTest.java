package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.ListLiteral.list;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static com.almondtools.conmatch.exceptions.ExceptionMatcher.matchesException;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.ArgumentRequiredException;
import com.almondtools.comtemplate.engine.DefaultErrorHandler;
import com.almondtools.comtemplate.engine.DefaultTemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.expressions.EvalAnonymousTemplate;
import com.almondtools.comtemplate.engine.expressions.EvalContextVar;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.StringLiteral;

public class ForTemplateTest {

	private TemplateInterpreter interpreter;

	@BeforeEach
	public void before() throws Exception {
		interpreter = new DefaultTemplateInterpreter(defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

	@Test
	public void testEvaluateEmptyList() throws Exception {
		ForTemplate template = new ForTemplate();
		ResolvedListLiteral list = new ResolvedListLiteral(emptyList());
		StringLiteral body = string("|");

		String result = template.evaluate(interpreter, var("var", list), var("do", body));

		assertThat(result, equalTo(""));
	}

	@Test
	public void testEvaluateSingleElementList() throws Exception {
		ForTemplate template = new ForTemplate();
		ResolvedListLiteral list = new ResolvedListLiteral(string("e"));
		StringLiteral body = string("|");

		String result = template.evaluate(interpreter, var("var", list), var("do", body));

		assertThat(result, equalTo("|"));
	}

	@Test
	public void testEvaluateMultiElementList() throws Exception {
		ForTemplate template = new ForTemplate();
		ResolvedListLiteral list = new ResolvedListLiteral(string("e1"), string("e2"));
		StringLiteral body = string("|");

		String result = template.evaluate(interpreter, var("var", list), var("do", body));

		assertThat(result, equalTo("||"));
	}

	@Test
	public void testEvaluateSingleElementListWithVariable() throws Exception {
		ForTemplate template = new ForTemplate();
		ResolvedListLiteral list = new ResolvedListLiteral(string("e"));
		EvalAnonymousTemplate body = new EvalAnonymousTemplate(template, new EvalContextVar("element"), string("|"));

		String result = template.evaluate(interpreter, var("element", list), var("do", body));

		assertThat(result, equalTo("e|"));
	}

	@Test
	public void testEvaluateMultiElementListWithVariable() throws Exception {
		ForTemplate template = new ForTemplate();
		ResolvedListLiteral list = new ResolvedListLiteral(string("e1"), string("e2"));
		EvalAnonymousTemplate body = new EvalAnonymousTemplate(template, new EvalContextVar("element"), string("|"));

		String result = template.evaluate(interpreter, var("element", list), var("do", body));

		assertThat(result, equalTo("e1|e2|"));
	}

	@Test
	public void testEvaluateVarIsNotList() throws Exception {
		ForTemplate template = new ForTemplate();
		EvalContextVar body = new EvalContextVar("ivar");

		String result = template.evaluate(interpreter, var("var", string("['a','b']")), var("do", body));

		assertThat(result, equalTo(""));
	}

	@Test
	public void testEvaluateVarIsNotDefined() throws Exception {
		ForTemplate template = new ForTemplate();
		EvalContextVar body = new EvalContextVar("var");

		ArgumentRequiredException thrown = assertThrows(ArgumentRequiredException.class, () -> template.evaluate(interpreter, var("do", body)));

		assertThat(thrown, matchesException(ArgumentRequiredException.class)
			.withMessage(containsString("argument <item> is required")));
	}

	@Test
	public void testEvaluateDoIsNotList() throws Exception {
		ForTemplate template = new ForTemplate();
		ArgumentRequiredException thrown = assertThrows(ArgumentRequiredException.class, () -> template.evaluate(interpreter, var("var", list(string("a")))));

		assertThat(thrown, matchesException(ArgumentRequiredException.class)
			.withMessage(containsString("argument <do> is required")));
	}

}
