package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static com.almondtools.comtemplate.parser.files.TemplateTests.compileLibrary;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class EscapingTest {

	private TemplateGroup group;

	@BeforeEach
	public void before() throws Exception {
		group = compileLibrary("src/test/resources/escaping.ctp");
	}

	@Test
	public void testEscapedExample1() throws Exception {
		String rendered = group.getDefinition("escapedExample1").evaluate(interpreter());
		assertThat(rendered, equalTo("<<escaped>>"));
	}

	@Test
	public void testEscapedExample2() throws Exception {
		String rendered = group.getDefinition("escapedExample2").evaluate(interpreter());
		assertThat(rendered, equalTo("<<escaped>>"));
	}

	@Test
	public void testNotEscapedExample() throws Exception {
		String rendered = group.getDefinition("notescapedExample").evaluate(interpreter());
		assertThat(rendered, equalTo("<notescaped>"));
	}

	@Test
	public void testEscapedHtml() throws Exception {
		String rendered = group.getDefinition("escapedHtml").evaluate(interpreter());
		assertThat(rendered, equalTo("<<<html>myhtml</html>>>"));
	}
}
