package com.almondtools.comtemplate.parser.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class ListsTest extends TemplateTests {

	private TemplateGroup group;

	@Before
	public void before() throws IOException {
		group = compileLibrary("src/test/resources/lists.ctp");
	}
	
	@Test
	public void testListItem() throws Exception {
		String rendered = group.getDefinition("listItem").evaluate();
		assertThat(rendered, equalTo("2"));
	}
	
	@Test
	public void testListSize() throws Exception {
		String rendered = group.getDefinition("listSize").evaluate();
		assertThat(rendered, equalTo("3"));
	}
	
	@Test
	public void testListFirst() throws Exception {
		String rendered = group.getDefinition("listFirst").evaluate();
		assertThat(rendered, equalTo("1"));
	}
	
	@Test
	public void testListRest() throws Exception {
		String rendered = group.getDefinition("listRest").evaluate();
		assertThat(rendered, equalTo("[2, 3]"));
	}
	
	@Test
	public void testListLast() throws Exception {
		String rendered = group.getDefinition("listLast").evaluate();
		assertThat(rendered, equalTo("3"));
	}
	
	@Test
	public void testListTrunc() throws Exception {
		String rendered = group.getDefinition("listTrunc").evaluate();
		assertThat(rendered, equalTo("[1, 2]"));
	}
	
	@Test
	public void testListStrip() throws Exception {
		String rendered = group.getDefinition("listStrip").evaluate();
		assertThat(rendered, equalTo("[1, 3]"));
	}
	
}
