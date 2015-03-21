package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateParameter.param;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.MapLiteral.map;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.almondtools.comtemplate.engine.expressions.EvalVar;

public class CustomObjectDefinitionTest {

	@Test
	public void testEvaluatePrimitiveType() throws Exception {
		CustomObjectDefinition object = new CustomObjectDefinition("object", "dec");
		object.setResult(new EvalVar("dec", object));

		assertThat(object.evaluate(var("dec", decimal(1.1))), equalTo("[_type=object, _value=1.1]"));
	}

	@Test
	public void testEvaluateObjectType() throws Exception {
		CustomObjectDefinition object = new CustomObjectDefinition("object", "dec", param("int", integer(2)));
		object.setResult(map(var("dec", new EvalVar("dec", object)), var("int", new EvalVar("int", object))));

		assertThat(object.evaluate(var("dec", decimal(1.1))), equalTo("[_type=object, dec=1.1, int=2]"));
	}

}
