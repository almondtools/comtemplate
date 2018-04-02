package net.amygdalum.comtemplate.engine;

import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static net.amygdalum.comtemplate.engine.TemplateParameter.param;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static net.amygdalum.comtemplate.engine.expressions.MapLiteral.map;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.ValueDefinition;
import net.amygdalum.comtemplate.engine.expressions.EvalVar;

public class CustomObjectDefinitionTest {

	@Test
	public void testEvaluateConstant() throws Exception {
		ValueDefinition object = new ValueDefinition("constant");
		object.setValue(string("string"));

		assertThat(object.evaluate(interpreter()), equalTo("string"));
	}

	@Test
	public void testToVariableConstant() throws Exception {
		ValueDefinition object = new ValueDefinition("constant");
		object.setValue(string("string"));
		
		assertThat(object.toVariable(), equalTo(var("constant", string("string"))));
	}

	@Test
	public void testGetValue() throws Exception {
		ValueDefinition object = new ValueDefinition("constant");
		object.setValue(string("string"));

		assertThat(object.getValue(), equalTo(string("string")));
	}

	@Test
	public void testEvaluatePrimitiveType() throws Exception {
		ValueDefinition object = new ValueDefinition("object", "dec");
		object.setObjectValue(new EvalVar("dec", object));

		assertThat(object.evaluate(interpreter(), var("dec", decimal(1.1))), equalTo("[_type=object, _value=1.1]"));
	}

	@Test
	public void testEvaluateObjectType() throws Exception {
		ValueDefinition object = new ValueDefinition("object", "dec", param("int", integer(2)));
		object.setObjectValue(map(var("dec", new EvalVar("dec", object)), var("int", new EvalVar("int", object))));

		assertThat(object.evaluate(interpreter(), var("dec", decimal(1.1))), equalTo("[_type=object, dec=1.1, int=2]"));
	}

}
