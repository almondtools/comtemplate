package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.almondtools.comtemplate.engine.expressions.EvalVar;
import com.almondtools.comtemplate.engine.expressions.RawText;

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
