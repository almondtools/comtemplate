package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.TemplateParameter.param;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static net.amygdalum.comtemplate.engine.expressions.MapLiteral.map;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.CustomTemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateGroup;

public class ExpressionEvaluationTest {

	@Test
	public void testEvaluateWithTemplateCallReferencedAnonymousTemplateArgumentWithStaticVariableReference() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");

		CustomTemplateDefinition temp = group.defineTemplate("temp", "arg");
		temp.add(new RawText("temp("));
		temp.add(new EvalVar("arg", temp));
		temp.add(new RawText(")"));

		CustomTemplateDefinition definition = group.defineTemplate("name", "callerArg");
		definition.add(new RawText("var: "));

		EvalAnonymousTemplate anonymousTemplate = new EvalAnonymousTemplate(definition, new EvalVar("callerArg", definition));

		definition.add(new EvalTemplate("temp", definition, var("arg", anonymousTemplate)));

		String evaluated = definition.evaluate(interpreter(), var("callerArg", string("callerArgument")));
		assertThat(evaluated, equalTo("var: temp(callerArgument)"));
	}

	@Test
	public void testEvaluateWithTemplateCallReferencedAnonymousTemplateArgumentWithContextVariableReference() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");

		CustomTemplateDefinition temp = group.defineTemplate("temp", "arg");
		temp.add(new RawText("temp("));
		temp.add(new EvalVar("arg", temp));
		temp.add(new RawText(")"));

		CustomTemplateDefinition definition = group.defineTemplate("name", "callerArg");
		definition.add(new RawText("var: "));

		EvalAnonymousTemplate anonymousTemplate = new EvalAnonymousTemplate(definition, new EvalContextVar("callerArg"));

		definition.add(new EvalTemplate("temp", definition, var("arg", anonymousTemplate)));

		String evaluated = definition.evaluate(interpreter(), var("callerArg", string("contextArgument")));
		assertThat(evaluated, equalTo("var: temp(contextArgument)"));
	}

	@Test
	public void testEvaluateWithTemplateCallReferencedValueArgument() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");

		CustomTemplateDefinition temp = group.defineTemplate("temp", "arg");
		temp.add(new RawText("temp("));
		temp.add(new EvalAttribute(new EvalVar("arg", temp), "field"));
		temp.add(new RawText(")"));

		CustomTemplateDefinition definition = group.defineTemplate("name", emptyList());
		definition.add(new RawText("var: "));
		definition.add(new EvalTemplate("temp", definition, var("arg", map(var("field", string("argument.field"))))));

		String evaluated = definition.evaluate(interpreter(), emptyList());
		assertThat(evaluated, equalTo("var: temp(argument.field)"));
	}

	@Test
	public void testEvaluateWithTemplateCallContextArgument() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");

		CustomTemplateDefinition temp = group.defineTemplate("temp", emptyList());
		temp.add(new RawText("temp("));
		temp.add(new EvalContextVar("callerArg"));
		temp.add(new RawText(")"));

		CustomTemplateDefinition definition = group.defineTemplate("name", "callerArg");
		definition.add(new RawText("var: "));
		definition.add(new EvalTemplate("temp", definition));

		String evaluated = definition.evaluate(interpreter(), var("callerArg", string("contextargument")));
		assertThat(evaluated, equalTo("var: temp(contextargument)"));
	}

	@Test
	public void testEvaluateWithTemplateFunctionCall() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");

		CustomTemplateDefinition temp = group.defineTemplate("temp", emptyList());
		temp.add(new RawText("\n some Text\n"));

		CustomTemplateDefinition definition = group.defineTemplate("name", emptyList());
		definition.add(new RawText("var: "));
		definition.add(new EvalFunction(new EvalTemplate("temp", definition), "trim"));

		String evaluated = definition.evaluate(interpreter(), emptyList());
		assertThat(evaluated, equalTo("var: some Text"));
	}

	@Test
	public void testEvaluateWithTemplateCallConstantArguments() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");

		CustomTemplateDefinition temp = group.defineTemplate("temp", "arg");
		temp.add(new RawText("temp("));
		temp.add(new EvalVar("arg", temp));
		temp.add(new RawText(")"));

		CustomTemplateDefinition definition = group.defineTemplate("name", emptyList());
		definition.add(new RawText("var: "));
		definition.add(new EvalTemplate("temp", definition, var("arg", string("argument"))));

		String evaluated = definition.evaluate(interpreter(), emptyList());
		assertThat(evaluated, equalTo("var: temp(argument)"));
	}

	@Test
	public void testEvaluateWithTemplateCallReferencedTemplateArgument() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");

		CustomTemplateDefinition argument = group.defineTemplate("argument", emptyList());
		argument.add(new RawText("argument"));

		CustomTemplateDefinition temp = group.defineTemplate("temp", "arg");
		temp.add(new RawText("temp("));
		temp.add(new EvalVar("arg", temp));
		temp.add(new RawText(")"));

		CustomTemplateDefinition definition = group.defineTemplate("name", emptyList());
		definition.add(new RawText("var: "));
		definition.add(new EvalTemplate("temp", definition, var("arg", new EvalTemplate("argument", definition))));

		String evaluated = definition.evaluate(interpreter(), emptyList());
		assertThat(evaluated, equalTo("var: temp(argument)"));
	}

	@Test
	public void testEvaluateWithDeclaredVariableUnset() throws Exception {
		CustomTemplateDefinition definition = new CustomTemplateDefinition("name", "var");
		definition.add(new RawText("var: "));
		definition.add(new EvalVar("var", definition));

		String result = definition.evaluate(interpreter(), emptyList());

		assertThat(result, equalTo("var: "));
	}

	@Test
	public void testEvaluateWithDeclaredVariableDefault() throws Exception {
		CustomTemplateDefinition definition = new CustomTemplateDefinition("name", param("var", string("default")));
		definition.add(new RawText("var: "));
		definition.add(new EvalVar("var", definition));

		String evaluated = definition.evaluate(interpreter(), emptyList());
		assertThat(evaluated, equalTo("var: default"));
	}

	@Test
	public void testEvaluateWithDeclaredVariableSet() throws Exception {
		CustomTemplateDefinition definition = new CustomTemplateDefinition("name", "var");
		definition.add(new RawText("var: "));
		definition.add(new EvalVar("var", definition));

		String evaluated = definition.evaluate(interpreter(), var("var", string("hello world")));
		assertThat(evaluated, equalTo("var: hello world"));
	}

	@Test
	public void testEvaluateWithUndeclaredVariable() throws Exception {
		CustomTemplateDefinition definition = new CustomTemplateDefinition("name", emptyList());
		definition.add(new RawText("var: "));
		definition.add(new EvalVar("var", definition));

		String result = definition.evaluate(interpreter(), emptyList());

		assertThat(result, equalTo("var: "));
	}

	@Test
	public void testEvaluateWithTemplateCallPassThroughArgument() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");

		CustomTemplateDefinition temp = group.defineTemplate("temp", "arg");
		temp.add(new RawText("temp("));
		temp.add(new EvalVar("arg", temp));
		temp.add(new RawText(")"));

		CustomTemplateDefinition definition = group.defineTemplate("name", "callerArg");
		definition.add(new RawText("var: "));
		definition.add(new EvalTemplate("temp", definition, var("arg", new EvalVar("callerArg", definition))));

		String evaluated = definition.evaluate(interpreter(), var("callerArg", string("callerargument")));
		assertThat(evaluated, equalTo("var: temp(callerargument)"));
	}

}
