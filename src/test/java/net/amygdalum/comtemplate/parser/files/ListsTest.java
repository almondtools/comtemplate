package net.amygdalum.comtemplate.parser.files;

import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.TemplateGroup;

public class ListsTest extends TemplateTests {

	private TemplateGroup group;

	@BeforeEach
	public void before() throws IOException {
		group = compileLibrary("src/test/resources/lists.ctp");
	}
	
	@Test
	public void testListItem() throws Exception {
		String rendered = group.getDefinition("listItem").evaluate(interpreter());
		assertThat(rendered, equalTo("2"));
	}
	
	@Test
	public void testListSize() throws Exception {
		String rendered = group.getDefinition("listSize").evaluate(interpreter());
		assertThat(rendered, equalTo("3"));
	}
	
	@Test
	public void testListFirst() throws Exception {
		String rendered = group.getDefinition("listFirst").evaluate(interpreter());
		assertThat(rendered, equalTo("1"));
	}
	
	@Test
	public void testListRest() throws Exception {
		String rendered = group.getDefinition("listRest").evaluate(interpreter());
		assertThat(rendered, equalTo("[2, 3]"));
	}
	
	@Test
	public void testListLast() throws Exception {
		String rendered = group.getDefinition("listLast").evaluate(interpreter());
		assertThat(rendered, equalTo("3"));
	}
	
	@Test
	public void testListTrunc() throws Exception {
		String rendered = group.getDefinition("listTrunc").evaluate(interpreter());
		assertThat(rendered, equalTo("[1, 2]"));
	}
	
	@Test
	public void testListStrip() throws Exception {
		String rendered = group.getDefinition("listStrip").evaluate(interpreter());
		assertThat(rendered, equalTo("[1, 3]"));
	}
	
}
