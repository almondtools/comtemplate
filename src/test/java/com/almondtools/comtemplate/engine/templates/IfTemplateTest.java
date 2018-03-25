package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.almondtools.comtemplate.engine.ArgumentRequiredException;
import com.almondtools.comtemplate.engine.DefaultErrorHandler;
import com.almondtools.comtemplate.engine.DefaultTemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateInterpreter;

public class IfTemplateTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private TemplateInterpreter interpreter;

	@Before
	public void before() throws Exception {
		interpreter = new DefaultTemplateInterpreter(defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

	@Test
	public void testEvaluateThenCase() throws Exception {
		IfTemplate template = new IfTemplate();
		String result = template.evaluate(interpreter, var("cond", TRUE), var("then", string("then")), var("else", string("else")));
		assertThat(result, equalTo("then"));
	}

	@Test
	public void testEvaluateElseCase() throws Exception {
		IfTemplate template = new IfTemplate();
		String result = template.evaluate(interpreter, var("cond", FALSE), var("then", string("then")), var("else", string("else")));
		assertThat(result, equalTo("else"));
	}

	@Test
	public void testEvaluateCondIsNotBoolean() throws Exception {
		IfTemplate template = new IfTemplate();

		String result = template.evaluate(interpreter, var("cond", string("not boolean")), var("then", string("then")), var("else", string("else")));

		assertThat(result, equalTo(""));
	}

	@Test
	public void testEvaluateCondIsNotDefined() throws Exception {
		IfTemplate template = new IfTemplate();
		thrown.expect(ArgumentRequiredException.class);

		String result = template.evaluate(interpreter, var("then", string("str")));

		assertThat(result, equalTo(""));
	}

	@Test
	public void testEvaluateThenIsNotList() throws Exception {
		IfTemplate template = new IfTemplate();
		thrown.expect(ArgumentRequiredException.class);

		String result = template.evaluate(interpreter, var("cond", TRUE));

		assertThat(result, equalTo(""));
	}

}
