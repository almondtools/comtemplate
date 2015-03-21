package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest {

	private TemplateDefinition definition;
	private List<TemplateVariable> variables;

	@Before
	public void before() throws Exception {
		definition = new TestTemplateDefinition("test");
		variables = emptyList();
	}

	@Test
	public void testGetParent() throws Exception {
		Scope parent = new Scope(definition, variables);
		assertThat(new Scope(parent, definition, variables).getParent(), sameInstance(parent));
	}

	@Test
	public void testGetDefinition() throws Exception {
		assertThat(new Scope(definition, variables).getDefinition(), sameInstance(definition));
	}

	@Test
	public void testGetVariables() throws Exception {
		assertThat(new Scope(definition, variables).getVariables(), sameInstance(variables));
	}

	@Test
	public void testResolveVariableInScope() throws Exception {
		assertThat(new Scope(definition, var("param", string("string"))).resolveVariable("param").getValue(), equalTo(string("string")));
	}

	@Test
	public void testResolveVariableToConstant() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		group.defineConstant(TemplateVariable.var("constant", string("string")));
		definition.setGroup(group);
		assertThat(new Scope(definition).resolveVariable("constant").getValue(), equalTo(string("string")));
	}

	@Test
	public void testResolveVariableInDistantDefinedScope() throws Exception {
		TemplateDefinition parentDefinition = new TestTemplateDefinition("parent");
		Scope parent = new Scope(parentDefinition, var("param", string("parentstring")));
		assertThat(new Scope(parent, definition, var("param", string("string"))).resolveVariable("param", parentDefinition).getValue(), equalTo(string("parentstring")));
	}

	@Test
	public void testResolveVariableInFailingDefinedScope() throws Exception {
		TemplateDefinition parentDefinition = new TestTemplateDefinition("parent");
		Scope parent = new Scope(parentDefinition, var("param", string("string")));
		assertThat(new Scope(parent, definition).resolveVariable("param", definition), nullValue());
	}

	@Test
	public void testResolveVariableFailsWithoutDefinitionAndParent() throws Exception {
		assertThat(new Scope(null).resolveVariable("param", definition), nullValue());
	}

	@Test
	public void testResolveContextVariable() throws Exception {
		assertThat(new Scope(definition, var("param", string("string"))).resolveContextVariable("param").getValue(), equalTo(string("string")));
	}

	@Test
	public void testResolveContextVariableInDistantDefinedScope() throws Exception {
		TemplateDefinition parentDefinition = new TestTemplateDefinition("parent");
		Scope parent = new Scope(parentDefinition, var("param", string("string")));
		assertThat(new Scope(parent, definition).resolveContextVariable("param").getValue(), equalTo(string("string")));
	}

	@Test
	public void testResolveContextVariableFails() throws Exception {
		TemplateDefinition parentDefinition = new TestTemplateDefinition("parent");
		Scope parent = new Scope(parentDefinition);
		assertThat(new Scope(parent, definition).resolveContextVariable("param"), nullValue());
	}

	@Test
	public void testResolveTemplateNoGroup() throws Exception {
		assertThat(new Scope(definition, var("param", string("string"))).resolveTemplate("call"), nullValue());
	}

	@Test
	public void testResolveTemplateGroupDefined() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		CustomTemplateDefinition calldef = group.defineTemplate("call");
		definition.setGroup(group);
		assertThat(new Scope(definition, var("param", string("string"))).resolveTemplate("call"), sameInstance(calldef));
	}

	@Test
	public void testResolveTemplateGroupImported() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		TestTemplateDefinition calldef = new TestTemplateDefinition("call");
		group.addImport(calldef);
		definition.setGroup(group);
		assertThat(new Scope(definition, var("param", string("string"))).resolveTemplate("call"), sameInstance(calldef));
	}

	@Test
	public void testResolveTemplateInDistantDefinedScope() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		TemplateDefinition parentDefinition = group.defineTemplate("parent");
		TemplateDefinition tempDefinition = group.defineTemplate("temp");
		Scope parent = new Scope(parentDefinition);
		assertThat(new Scope(parent, definition).resolveTemplate("temp", parentDefinition), sameInstance(tempDefinition));
	}

	@Test
	public void testResolveTemplateInFailingDefinedScope() throws Exception {
		TemplateDefinition parentDefinition = new TestTemplateDefinition("parent");
		Scope parent = new Scope(parentDefinition);
		assertThat(new Scope(parent, definition).resolveTemplate("temp", definition), nullValue());
	}

	@Test
	public void testResolveTemplateFailsWithoutDefinitionAndParent() throws Exception {
		assertThat(new Scope(null).resolveTemplate("temp", definition), nullValue());
	}

}
