package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static com.almondtools.comtemplate.parser.files.TemplateTests.compileLibrary;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.almondtools.comtemplate.engine.DefaultErrorHandler;
import com.almondtools.comtemplate.engine.DefaultTemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateLoader;
import com.almondtools.comtemplate.engine.expressions.BooleanLiteral;

public class TestTest {

	private TemplateLoader loader;
	private TemplateGroup group;
	private TemplateInterpreter interpreter;

	@BeforeEach
	public void before() throws Exception {
		loader = Mockito.mock(TemplateLoader.class);
		group = compileLibrary("src/test/resources/test.ctp");
		interpreter = new DefaultTemplateInterpreter(loader, defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
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
