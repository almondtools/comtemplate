package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static com.almondtools.comtemplate.parser.files.TemplateTests.compileLibrary;
import static org.hamcrest.CoreMatchers.containsString;
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
		String rendered = group.getDefinition("ifThenElse").evaluate(interpreter());
		assertThat(rendered, equalTo("cond was false"));
	}

	@Test
	public void testIfThen() throws Exception {
		String rendered = group.getDefinition("ifThen").evaluate(interpreter());
		assertThat(rendered, equalTo("cond was true"));
	}

	@Test
	public void testIfWithAny() throws Exception {
		String rendered = group.getDefinition("ifWithAny").evaluate(interpreter());
		assertThat(rendered, equalTo("any(true,false)=true"));
	}

	@Test
	public void testIfWithNotAny() throws Exception {
		String rendered = group.getDefinition("ifWithNotAny").evaluate(interpreter());
		assertThat(rendered, equalTo("any(false,false)=false"));
	}

	@Test
	public void testIfWithAll() throws Exception {
		String rendered = group.getDefinition("ifWithAll").evaluate(interpreter());
		assertThat(rendered, equalTo("all(true,true)=true"));
	}

	@Test
	public void testIfWithNotAll() throws Exception {
		String rendered = group.getDefinition("ifWithNotAll").evaluate(interpreter());
		assertThat(rendered, equalTo("all(true,false)=false"));
	}

	@Test
	public void testIfWithNot() throws Exception {
		String rendered = group.getDefinition("ifWithNot").evaluate(interpreter());
		assertThat(rendered, containsString("not(true)=false"));
		assertThat(rendered, containsString("not(false)=true"));
	}

}
