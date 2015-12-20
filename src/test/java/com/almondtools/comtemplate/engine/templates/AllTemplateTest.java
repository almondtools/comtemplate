package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.ListLiteral.list;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AllTemplateTest {

	@Test
	public void testEvaluateTrue() throws Exception {
		AllTemplate template = new AllTemplate();
		String result = template.evaluate(var("cond", TRUE));
		assertThat(result, equalTo("true"));
	}

	@Test
	public void testEvaluateFalse() throws Exception {
		AllTemplate template = new AllTemplate();
		String result = template.evaluate(var("cond", FALSE));
		assertThat(result, equalTo("false"));
	}

	@Test
	public void testEvaluateAllTrue() throws Exception {
		AllTemplate template = new AllTemplate();
		String result = template.evaluate(var("cond", list(TRUE, TRUE)));
		assertThat(result, equalTo("true"));
	}

	@Test
	public void testEvaluateSomeTrue() throws Exception {
		AllTemplate template = new AllTemplate();
		String result = template.evaluate(var("cond", list(TRUE, FALSE)));
		assertThat(result, equalTo("false"));
	}

	@Test
	public void testEvaluateNoneTrue() throws Exception {
		AllTemplate template = new AllTemplate();
		String result = template.evaluate(var("cond", list(FALSE, FALSE)));
		assertThat(result, equalTo("false"));
	}

	@Test
	public void testEvaluateCondIsNotBoolean() throws Exception {
		AllTemplate template = new AllTemplate();

		String result = template.evaluate(var("cond", string("not boolean")));

		assertThat(result, equalTo(""));
	}

}
