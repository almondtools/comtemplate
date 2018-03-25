package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static com.almondtools.comtemplate.parser.files.TemplateTests.compileLibrary;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.DefaultErrorHandler;
import com.almondtools.comtemplate.engine.DefaultTemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.expressions.BooleanLiteral;

public class TestTest {

	private TemplateGroup group;
	private TemplateInterpreter interpreter;

	@Before
	public void before() throws Exception {
		group = compileLibrary("src/test/resources/test.ctp");
		interpreter = new DefaultTemplateInterpreter(defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

	@Test
	public void testTestTrue() throws Exception {
		BooleanLiteral results = (BooleanLiteral) group.getDefinition("test").evaluate(interpreter, null, emptyList());
		assertThat(results.as(Boolean.class), is(true));
	}

	@Test
	public void testTestFalse() throws Exception {
		BooleanLiteral results = (BooleanLiteral) group.getDefinition("fail").evaluate(interpreter, null, emptyList());
		assertThat(results.as(Boolean.class), is(false));
	}

}
