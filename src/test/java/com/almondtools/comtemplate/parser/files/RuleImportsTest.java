package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.ConfigurableTemplateLoader;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateLoader;

public class RuleImportsTest extends TemplateTests {

	private TemplateLoader loader;
	private TemplateGroup group;

	@BeforeEach
	public void before() throws Exception {
		loader = new ConfigurableTemplateLoader()
			.withClasspath(true)
			.forPaths("src/test/resources");
		group = loader.loadGroup("ruleimports");
	}

	@Test
	public void testBrackets() throws Exception {
		String rendered = group.getDefinition("testbrackets").evaluate(interpreter());
		assertThat(rendered, equalTo("brackets: [content]"));
	}

	@Test
	public void testBraces() throws Exception {
		String rendered = group.getDefinition("testbraces").evaluate(interpreter());
		assertThat(rendered, equalTo("braces: {content}"));
	}

	@Test
	public void testLocalImport() throws Exception {
		String rendered = group.getDefinition("testlocal1").evaluate(interpreter());
		assertThat(rendered, equalTo("local1"));
	}

	@Test
	public void testNotLocalImport() throws Exception {
		String rendered = group.getDefinition("testlocal2").evaluate(interpreter());
		assertThat(rendered, equalTo(""));
	}

}
