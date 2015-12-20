package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.parser.files.TemplateTests.compileLibrary;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class ForTest {

	private TemplateGroup group;

	@Before
	public void before() throws Exception {
		group = compileLibrary("src/test/resources/for.ctp");
	}
	
	@Test
	public void testForInDefault() throws Exception {
		String rendered = group.getDefinition("forInDefault").evaluate();
		assertThat(rendered, equalTo("  :a:b"));
	}
	
	@Test
	public void testForInList() throws Exception {
		String rendered = group.getDefinition("forInList").evaluate();
		assertThat(rendered, equalTo("   1:1 2:2"));
	}
	
	@Test
	public void testForInListI() throws Exception {
		String rendered = group.getDefinition("forInListI").evaluate();
		assertThat(rendered, equalTo("   1:a 2:b"));
	}
	
	@Test
	public void testForInListI0() throws Exception {
		String rendered = group.getDefinition("forInListI0").evaluate();
		assertThat(rendered, equalTo("   0:a 1:b"));
	}
	
}
