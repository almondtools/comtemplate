package com.almondtools.comtemplate.parser.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.ConfigurableTemplateLoader;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateLoader;

public class WildcardImportsTest extends TemplateTests {

	private TemplateLoader loader;
	private TemplateGroup group;

	@Before
	public void before() throws Exception {
		loader = new ConfigurableTemplateLoader()
			.withClasspath(true)
			.forPaths("src/test/resources");
		group = loader.loadGroup("wildcardimports");
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

	@Test
	public void testParentheses() throws Exception {
		String rendered = group.getDefinition("testparentheses").evaluate();
		assertThat(rendered, equalTo("parentheses: (content)"));
	}

	@Test
	public void testLocal1Import() throws Exception {
		String rendered = group.getDefinition("testlocal1").evaluate();
		assertThat(rendered, equalTo("local1"));
	}

	@Test
	public void testLocal2Import() throws Exception {
		String rendered = group.getDefinition("testlocal2").evaluate();
		assertThat(rendered, equalTo("local2"));
	}

}
