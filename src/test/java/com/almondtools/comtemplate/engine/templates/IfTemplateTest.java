package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.almondtools.comtemplate.engine.ArgumentRequiredException;
import com.almondtools.comtemplate.engine.DefaultErrorHandler;
import com.almondtools.comtemplate.engine.DefaultTemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateLoader;

public class IfTemplateTest {

	private TemplateLoader loader;
	private TemplateInterpreter interpreter;

	@BeforeEach
	public void before() throws Exception {
		loader = Mockito.mock(TemplateLoader.class);
		interpreter = new DefaultTemplateInterpreter(loader, defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
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
		assertThrows(ArgumentRequiredException.class, () -> template.evaluate(interpreter, var("then", string("str"))));
	}

	@Test
	public void testEvaluateThenIsNotList() throws Exception {
		IfTemplate template = new IfTemplate();
		assertThrows(ArgumentRequiredException.class, () -> template.evaluate(interpreter, var("cond", TRUE)));
	}

}
