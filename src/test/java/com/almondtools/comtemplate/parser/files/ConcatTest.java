package com.almondtools.comtemplate.parser.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class ConcatTest extends TemplateTests {

	private TemplateGroup group;

	@Before
	public void before() throws IOException {
		group = compileFile("src/test/resources/concat.ctp");
	}
	
	@Test
	public void testConcatLists() throws Exception {
		String rendered = group.getDefinition("concatLists").evaluate();
		assertThat(rendered, equalTo("1 2 | 3 4"));
	}
	
	@Test
	public void testConcatMaps() throws Exception {
		String rendered = group.getDefinition("concatMaps").evaluate();
		assertThat(rendered, equalTo("a b -> 1 2"));
	}

	
}
