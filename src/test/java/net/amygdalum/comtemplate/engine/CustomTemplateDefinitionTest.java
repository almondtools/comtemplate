package net.amygdalum.comtemplate.engine;

import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.CustomTemplateDefinition;
import net.amygdalum.comtemplate.engine.expressions.EvalVar;
import net.amygdalum.comtemplate.engine.expressions.RawText;

public class CustomTemplateDefinitionTest {

	@Test
	public void testEvaluateSimple() throws Exception {
		CustomTemplateDefinition template = new CustomTemplateDefinition("template");
		template.add(new RawText("raw text"));

		assertThat(template.evaluate(interpreter()), equalTo("raw text"));
	}

	@Test
	public void testEvaluateVariables() throws Exception {
		CustomTemplateDefinition template = new CustomTemplateDefinition("template", "var");
		template.add(new RawText("variable:"));
		template.add(new EvalVar("var", template));

		assertThat(template.evaluate(interpreter(), var("var", string("value"))), equalTo("variable:value"));
	}

}
