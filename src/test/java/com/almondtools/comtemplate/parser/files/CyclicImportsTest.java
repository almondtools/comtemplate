package com.almondtools.comtemplate.parser.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.ClassPathTemplateLoader;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateLoader;

public class CyclicImportsTest extends TemplateTests {

	private TemplateLoader loader;
	private TemplateGroup group;

	@Before
	public void before() throws Exception {
		loader = new ClassPathTemplateLoader().addClassPath("src/test/resources");
		group = loader.loadGroup("cyclicimports");
	}
	
	@Test
	public void testAdd() throws Exception {
		String rendered = group.getDefinition("testadd").evaluate();
		assertThat(rendered, equalTo("3 = 1 + 2"));
	}
	
	@Test
	public void testMul() throws Exception {
		String rendered = group.getDefinition("testmul").evaluate();
		assertThat(rendered, equalTo("2 = 1 * 2"));
	}
	
}
