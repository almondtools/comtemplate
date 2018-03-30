package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.almondtools.comtemplate.engine.DefaultErrorHandler;
import com.almondtools.comtemplate.engine.DefaultTemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateLoader;

public class NotTemplateTest {

	private TemplateLoader loader;
	private TemplateInterpreter interpreter;

	@BeforeEach
	public void before() throws Exception {
		loader = Mockito.mock(TemplateLoader.class);
		interpreter = new DefaultTemplateInterpreter(loader, defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

	@Test
	public void testEvaluateTrue() throws Exception {
		NotTemplate template = new NotTemplate();
		String result = template.evaluate(interpreter, var("cond", TRUE));
		assertThat(result, equalTo("false"));
	}

	@Test
	public void testEvaluateFalse() throws Exception {
		NotTemplate template = new NotTemplate();
		String result = template.evaluate(interpreter, var("cond", FALSE));
		assertThat(result, equalTo("true"));
	}

	@Test
	public void testEvaluateCondIsNotBoolean() throws Exception {
		NotTemplate template = new NotTemplate();

		String result = template.evaluate(interpreter, var("cond", string("not boolean")));

		assertThat(result, equalTo(""));
	}

}
