package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateParameter.param;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

public class TemplateDefinitionTest {

	@Test
	public void testGetGroup() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		TestTemplateDefinition def = new TestTemplateDefinition("name");
		def.setGroup(group);

		assertThat(def.getGroup(), equalTo(group));
	}

	@Test
	public void testGetName() throws Exception {
		TestTemplateDefinition def = new TestTemplateDefinition("name");

		assertThat(def.getName(), equalTo("name"));
	}

	@Test
	public void testGetParameters() throws Exception {
		TestTemplateDefinition def = new TestTemplateDefinition("name", "a", "b");

		assertThat(def.getParameters(), contains(param("a"), param("b")));
	}

	@Test
	public void testEvaluateNative() throws Exception {
		TestTemplateDefinition def = new TestTemplateDefinition("name", "a", "b");

		String evaluated = def.evaluateNative(interpreter(), 1, 2);

		assertThat(evaluated, equalTo("test: a=1,b=2"));
	}

	@Test
	public void testEvaluate() throws Exception {
		TestTemplateDefinition def = new TestTemplateDefinition("name", "a", param("b", integer(2)));

		String evaluated = def.evaluate(interpreter(), var("a", integer(1)));

		assertThat(evaluated, equalTo("test: a=1"));
	}

	@Test
	public void testEvaluateWithScope() throws Exception {
		TestTemplateDefinition def = new TestTemplateDefinition("name", "a", param("b", integer(2)));

		String evaluated = def.evaluate(interpreter(), new Scope(def, var("a", integer(1))));

		assertThat(evaluated, equalTo("test: a=1"));
	}

	@Test
	public void testCreateVariables() throws Exception {
		TestTemplateDefinition def = new TestTemplateDefinition("name", "a", "b");
		TemplateVariable a = var("a", string("A"));
		TemplateVariable b = var("b", string("B"));
		TemplateVariable c = var("c", string("C"));

		assertThat(def.createVariables(asList(a)), contains(a));
		assertThat(def.createVariables(asList(a, b, c)), contains(a, b));
	}

	@Test
	public void testCreateVariablesDefault() throws Exception {
		TestTemplateDefinition def = new TestTemplateDefinition("name", "a", "b", param("c", string("C")));
		TemplateVariable a = var("a", string("A"));
		TemplateVariable b = var("b", string("B"));
		TemplateVariable c = var("c", string("C"));

		assertThat(def.createVariables(asList(a)), contains(a, c));
		assertThat(def.createVariables(asList(a, b)), contains(a, b, c));
	}

	@Test
	public void testFindVariable() throws Exception {
		TestTemplateDefinition def = new TestTemplateDefinition("name", "a", "b");
		TemplateVariable a = var("a", string("A"));
		TemplateVariable b = var("b", string("B"));

		assertThat(def.findVariable("a", asList(a, b)).get(), equalTo(a));
		assertThat(def.findVariable("b", asList(a, b)).get(), equalTo(b));
		assertThat(def.findVariable("c", asList(a, b)).isPresent(), is(false));
	}

}
