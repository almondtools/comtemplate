package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.parser.files.TemplateTests.compileLibrary;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class ApplyTest {

	private TemplateGroup group;

	@Before
	public void before() throws Exception {
		group = compileLibrary("src/test/resources/apply.ctp");
	}

	@Test
	public void testApplyObject() throws Exception {
		String rendered = group.getDefinition("applyObject").evaluate();
		assertThat(rendered, equalTo("myname"));
	}

	@Test
	public void testApplyTemplate() throws Exception {
		String rendered = group.getDefinition("applyTemplate").evaluate();
		assertThat(rendered, equalTo("mycontent"));
	}
}
