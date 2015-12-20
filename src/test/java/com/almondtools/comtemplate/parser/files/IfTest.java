package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.parser.files.TemplateTests.compileLibrary;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class IfTest {

	private TemplateGroup group;

	@Before
	public void before() throws Exception {
		group = compileLibrary("src/test/resources/if.ctp");
	}

	@Test
	public void testIfThenElse() throws Exception {
		String rendered = group.getDefinition("ifThenElse").evaluate();
		assertThat(rendered, equalTo("cond was false"));
	}

	@Test
	public void testIfThen() throws Exception {
		String rendered = group.getDefinition("ifThen").evaluate();
		assertThat(rendered, equalTo("cond was true"));
	}

}
