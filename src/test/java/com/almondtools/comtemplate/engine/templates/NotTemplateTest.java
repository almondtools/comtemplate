package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class NotTemplateTest {

	@Test
	public void testEvaluateTrue() throws Exception {
		NotTemplate template = new NotTemplate();
		String result = template.evaluate(var("cond", TRUE));
		assertThat(result, equalTo("false"));
	}

	@Test
	public void testEvaluateFalse() throws Exception {
		NotTemplate template = new NotTemplate();
		String result = template.evaluate(var("cond", FALSE));
		assertThat(result, equalTo("true"));
	}

	@Test
	public void testEvaluateCondIsNotBoolean() throws Exception {
		NotTemplate template = new NotTemplate();

		String result = template.evaluate(var("cond", string("not boolean")));

		assertThat(result, equalTo(""));
	}

}
