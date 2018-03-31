package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class VariablesTest extends TemplateTests {

	private TemplateGroup group;

	@BeforeEach
	public void before() throws IOException {
		group = compileLibrary("src/test/resources/variables.ctp");
	}
	
	@Test
	public void testWithContext() throws Exception {
		String rendered = group.getDefinition("withContext").evaluate(interpreter());
		assertThat(rendered, equalTo("someContext"));
	}

	@Test
	public void testWithoutContext() throws Exception {
		String rendered = group.getDefinition("withoutContext").evaluate(interpreter());
		assertThat(rendered, equalTo("nothing"));
	}
	
	@Test
	public void testDefaultWithContext() throws Exception {
		String rendered = group.getDefinition("defaultWithContext").evaluate(interpreter());
		assertThat(rendered, equalTo("someContext"));
	}
	
	@Test
	public void testDefaultWithoutContext() throws Exception {
		String rendered = group.getDefinition("defaultWithoutContext").evaluate(interpreter());
		assertThat(rendered, equalTo("nothing"));
	}
	
	@Test
	public void testIgnoringErrorsWithSufficientContext() throws Exception {
		String rendered = group.getDefinition("ignoringErrorsWithSufficientContext").evaluate(interpreter());
		assertThat(rendered, equalTo("next"));
	}
	
	@Test
	public void testIgnoringErrorsWithAlmostSufficientContext() throws Exception {
		String rendered = group.getDefinition("ignoringErrorsWithAlmostSufficientContext").evaluate(interpreter());
		assertThat(rendered, equalTo("nothing"));
	}
	
	@Test
	public void testIgnoringErrorsWithContext() throws Exception {
		String rendered = group.getDefinition("ignoringErrorsWithContext").evaluate(interpreter());
		assertThat(rendered, equalTo("nothing"));
	}
	
	@Test
	public void testIgnoringErrorsWithoutContext() throws Exception {
		String rendered = group.getDefinition("ignoringErrorsWithoutContext").evaluate(interpreter());
		assertThat(rendered, equalTo("nothing"));
	}
	
}
