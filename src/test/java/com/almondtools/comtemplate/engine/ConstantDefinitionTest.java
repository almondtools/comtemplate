package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ConstantDefinitionTest {

	@Test
	public void testEvaluate() throws Exception {
		ConstantDefinition object = new ConstantDefinition("constant");
		object.setValue(string("string"));

		assertThat(object.evaluate(), equalTo("string"));
	}

	@Test
	public void testToVariable() throws Exception {
		ConstantDefinition object = new ConstantDefinition("constant");
		object.setValue(string("string"));
		
		assertThat(object.toVariable(), equalTo(TemplateVariable.var("constant", string("string"))));
	}

	@Test
	public void testGetValue() throws Exception {
		ConstantDefinition object = new ConstantDefinition("constant");
		object.setValue(string("string"));

		assertThat(object.getValue(), equalTo(string("string")));
	}

}
