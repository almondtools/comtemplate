package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class ValuesTest extends TemplateTests {

	private TemplateGroup group;

	@BeforeEach
	public void before() throws Exception {
		group = compileLibrary("src/test/resources/values.ctp");
	}

	@Test
	public void testMap() throws Exception {
		String rendered = group.getDefinition("rendermap").evaluate(interpreter());
		assertThat(rendered, equalTo("map = [name=name,type=type]"));
	}

	@Test
	public void testDuplicateMap() throws Exception {
		String rendered = group.getDefinition("renderduplicatemap").evaluate(interpreter());
		assertThat(rendered, equalTo("map = [name=value,type=type]"));
	}

	@Test
	public void testList() throws Exception {
		String rendered = group.getDefinition("renderlist").evaluate(interpreter());
		assertThat(rendered, equalTo("list = [element1,element2]"));
	}

	@Test
	public void testInt() throws Exception {
		String rendered = group.getDefinition("renderint").evaluate(interpreter());
		assertThat(rendered, equalTo("int = 22"));
	}

	@Test
	public void testDec() throws Exception {
		String rendered = group.getDefinition("renderdec").evaluate(interpreter());
		assertThat(rendered, equalTo("dec = -0.2"));
	}

	@Test
	public void testBool() throws Exception {
		String rendered = group.getDefinition("renderbool").evaluate(interpreter());
		assertThat(rendered, equalTo("bool = false"));
	}

}
