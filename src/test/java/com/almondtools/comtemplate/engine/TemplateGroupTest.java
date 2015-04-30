package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateParameter.param;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TemplateGroupTest {

	@Test
	public void testGetName() throws Exception {
		assertThat(new TemplateGroup("group").getName(), equalTo("group"));
	}

	@Test
	public void testGetImportsEmpty() throws Exception {
		assertThat(new TemplateGroup("group").getImports(), empty());
	}

	@Test
	public void testGetImportsNotEmpty() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		TemplateDefinition definition = new TestTemplateDefinition("template");
		group.addImport(definition);
		assertThat(group.getImports(), contains(definition));
	}

	@Test
	public void testGetDefinitionsEmpty() throws Exception {
		assertThat(new TemplateGroup("group").getDefinitions(), empty());
	}

	@Test
	public void testGetDefinitionsNotEmpty() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		TemplateDefinition definition = new TestTemplateDefinition("template");
		group.define(definition);
		assertThat(group.getDefinitions(), contains(definition));
	}

	@Test
	public void testGetConstantsNotEmpty() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		ConstantDefinition constant = group.defineConstant("constant");
		constant.setValue(string("constant"));
		assertThat(group.getDefinitions(), contains(constant));
	}

	@Test
	public void testGetDefinitionNotFound() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		assertThat(group.getDefinition("template"), nullValue());
	}

	@Test
	public void testGetDefinitionFound() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		CustomTemplateDefinition template = group.defineTemplate("template");
		assertThat(group.getDefinition("template"), equalTo(template));
	}

	@Test
	public void testGetDefinitionFromImport() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		TestTemplateDefinition template = new TestTemplateDefinition("template");
		group.addImport(template);
		assertThat(group.getDefinition("template"), equalTo(template));
	}

	@Test
	public void testResolveVariableNotFound() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		assertThat(group.resolveVariable("var"), nullValue());
	}

	@Test
	public void testResolveVariableFound() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		group.defineConstant("var").setValue(string("string"));
		assertThat(group.resolveVariable("var"), equalTo(var("var", string("string"))));
	}

	@Test
	public void testDefineTemplate() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		CustomTemplateDefinition template = group.defineTemplate("template", "a", "b");

		assertThat(group.getDefinition("template"), equalTo(template));
		assertThat(template.getName(), equalTo("template"));
		assertThat(template.getParameters(), contains(param("a"), param("b")));
	}

	@Test
	public void testDefineObject() throws Exception {
		TemplateGroup group = new TemplateGroup("group");
		CustomObjectDefinition object = group.defineObject("object", "c", "d");

		assertThat(group.getDefinition("object"), equalTo(object));
		assertThat(object.getName(), equalTo("object"));
		assertThat(object.getParameters(), contains(param("c"), param("d")));
	}

}
