package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateParameter.param;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

public class TemplateGroupTest {

	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Test
	public void testGetName() throws Exception {
		assertThat(new TemplateGroup("group", "testresource").getName(), equalTo("group"));
	}

	@Test
	public void testGetResource() throws Exception {
		assertThat(new TemplateGroup("group", "testresource").getResource(), equalTo("testresource"));
	}

	@Test
	public void testGetImportsEmpty() throws Exception {
		assertThat(new TemplateGroup("group", "testresource").getImports(), empty());
	}

	@Test
	public void testGetImportsNotEmpty() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		TemplateDefinition definition = new TestTemplateDefinition("template");
		group.addImport(definition);
		assertThat(group.getImports(), contains(definition));
	}

	@Test
	public void testGetDefinitionsEmpty() throws Exception {
		assertThat(new TemplateGroup("group", "testresource").getDefinitions(), empty());
	}

	@Test
	public void testGetDefinitionsNotEmpty() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		TemplateDefinition definition = new TestTemplateDefinition("template");
		group.define(definition);
		assertThat(group.getDefinitions(), contains(definition));
	}

	@Test
	public void testGetConstantsNotEmpty() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		ValueDefinition constant = group.defineConstant("constant");
		constant.setValue(string("constant"));
		assertThat(group.getDefinitions(), contains(constant));
	}

	@Test
	public void testGetDefinitionNotFound() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");

		assertThat(group.getDefinition("template"), nullValue());
	}

	@Test
	public void testGetDefinitionFound() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		CustomTemplateDefinition template = group.defineTemplate("template");
		assertThat(group.getDefinition("template"), equalTo(template));
	}

	@Test
	public void testGetDefinitionFromImport() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		TestTemplateDefinition template = new TestTemplateDefinition("template");
		group.addImport(template);
		assertThat(group.getDefinition("template"), equalTo(template));
	}

	@Test
	public void testResolveVariableNotFound() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		assertThat(group.resolveVariable("var").isPresent(), is(false));
	}

	@Test
	public void testResolveVariableFound() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		group.defineConstant("var").setValue(string("string"));
		assertThat(group.resolveVariable("var").get(), equalTo(var("var", string("string"))));
	}

	@Test
	public void testDefineTemplate() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		CustomTemplateDefinition template = group.defineTemplate("template", "a", "b");

		assertThat(group.getDefinition("template"), equalTo(template));
		assertThat(template.getName(), equalTo("template"));
		assertThat(template.getParameters(), contains(param("a"), param("b")));
	}

	@Test
	public void testDefineObject() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		ValueDefinition object = group.defineObject("object", "c", "d");

		assertThat(group.getDefinition("object"), equalTo(object));
		assertThat(object.getName(), equalTo("object"));
		assertThat(object.getParameters(), contains(param("c"), param("d")));
	}

	@Test
	public void testRelativeReference() throws Exception {
		TemplateGroup group = new TemplateGroup("path.group", "testresource");
		assertThat(group.relativeReference("other"), equalTo("path.other"));
	}

	@Test
	public void testRelativeReferenceWithoutPath() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		assertThat(group.relativeReference("other"), equalTo("other"));
	}

}
