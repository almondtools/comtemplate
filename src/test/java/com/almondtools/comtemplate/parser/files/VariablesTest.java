package com.almondtools.comtemplate.parser.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class VariablesTest extends TemplateTests {

	private TemplateGroup group;

	@Before
	public void before() throws IOException {
		group = compileFile("src/test/resources/variables.ctp");
	}
	
	@Test
	public void testWithContext() throws Exception {
		String rendered = group.getDefinition("withContext").evaluate();
		assertThat(rendered, equalTo("someContext"));
	}

	@Test
	public void testWithoutContext() throws Exception {
		String rendered = group.getDefinition("withoutContext").evaluate();
		assertThat(rendered, equalTo("nothing"));
	}
	
	@Test
	public void testDefaultWithContext() throws Exception {
		String rendered = group.getDefinition("defaultWithContext").evaluate();
		assertThat(rendered, equalTo("someContext"));
	}
	
	@Test
	public void testDefaultWithoutContext() throws Exception {
		String rendered = group.getDefinition("defaultWithoutContext").evaluate();
		assertThat(rendered, equalTo("nothing"));
	}
	
}
