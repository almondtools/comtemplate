package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.ListLiteral.list;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.DefaultErrorHandler;
import com.almondtools.comtemplate.engine.DefaultTemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateInterpreter;

public class AnyTemplateTest {

	private TemplateInterpreter interpreter;

	@BeforeEach
	public void before() throws Exception {
		interpreter = new DefaultTemplateInterpreter(defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

	@Test
	public void testEvaluateTrue() throws Exception {
		AnyTemplate template = new AnyTemplate();
		String result = template.evaluate(interpreter,var("cond", TRUE));
		assertThat(result, equalTo("true"));
	}

	@Test
	public void testEvaluateFalse() throws Exception {
		AnyTemplate template = new AnyTemplate();
		String result = template.evaluate(interpreter,var("cond", FALSE));
		assertThat(result, equalTo("false"));
	}

	@Test
	public void testEvaluateAllTrue() throws Exception {
		AnyTemplate template = new AnyTemplate();
		String result = template.evaluate(interpreter,var("cond", list(TRUE, TRUE)));
		assertThat(result, equalTo("true"));
	}

	@Test
	public void testEvaluateSomeTrue() throws Exception {
		AnyTemplate template = new AnyTemplate();
		String result = template.evaluate(interpreter,var("cond", list(TRUE, FALSE)));
		assertThat(result, equalTo("true"));
	}

	@Test
	public void testEvaluateNoneTrue() throws Exception {
		AnyTemplate template = new AnyTemplate();
		String result = template.evaluate(interpreter,var("cond", list(FALSE, FALSE)));
		assertThat(result, equalTo("false"));
	}

	@Test
	public void testEvaluateCondIsNotBoolean() throws Exception {
		AnyTemplate template = new AnyTemplate();

		String result = template.evaluate(interpreter,var("cond", string("not boolean")));

		assertThat(result, equalTo(""));
	}

}
