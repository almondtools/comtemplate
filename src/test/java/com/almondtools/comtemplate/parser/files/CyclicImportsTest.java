package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.ConfigurableTemplateLoader;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateLoader;

public class CyclicImportsTest extends TemplateTests {

	private TemplateLoader loader;
	private TemplateGroup group;

	@BeforeEach
	public void before() throws Exception {
		loader = new ConfigurableTemplateLoader()
			.withClasspath(true)
			.forPaths("src/test/resources");
		group = loader.loadGroup("cyclicimports");
	}

	@Test
	public void testAdd() throws Exception {
		String rendered = group.getDefinition("testadd").evaluate(interpreter());
		assertThat(rendered, equalTo("3 = 1 + 2"));
	}

	@Test
	public void testMul() throws Exception {
		String rendered = group.getDefinition("testmul").evaluate(interpreter());
		assertThat(rendered, equalTo("2 = 1 * 2"));
	}

}
