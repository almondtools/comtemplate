package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static com.almondtools.comtemplate.parser.files.TemplateTests.compileLibrary;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class IfTest {

	private TemplateGroup group;

	@BeforeEach
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

	@Test
	public void testIfLazy() throws Exception {
		String renderedExisting = group.getDefinition("ifLazy").evaluate(interpreter(), var("field", string("field")));
		assertThat(renderedExisting, equalTo("field = value"));
		String renderedNotExisting = group.getDefinition("ifLazy").evaluate(interpreter(), var("field", string("otherfield")));
		assertThat(renderedNotExisting, equalTo("otherfield not found"));
	}

}
