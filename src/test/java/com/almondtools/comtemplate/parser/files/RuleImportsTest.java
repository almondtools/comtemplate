package com.almondtools.comtemplate.parser.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.ClassPathTemplateLoader;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateLoader;

public class RuleImportsTest extends TemplateTests {

	private TemplateLoader loader;
	private TemplateGroup group;

	@Before
	public void before() throws Exception {
		loader = new ClassPathTemplateLoader().addClassPath("src/test/resources");
		group = loader.loadGroup("ruleimports");
	}

	@Test
	public void testBrackets() throws Exception {
		String rendered = group.getDefinition("testbrackets").evaluate();
		assertThat(rendered, equalTo("brackets: [content]"));
	}

	@Test
	public void testBraces() throws Exception {
		String rendered = group.getDefinition("testbraces").evaluate();
		assertThat(rendered, equalTo("braces: {content}"));
	}

}
