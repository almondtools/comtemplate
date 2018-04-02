package net.amygdalum.comtemplate.engine;

import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.CustomTemplateDefinition;
import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateGroup;
import net.amygdalum.comtemplate.engine.TemplateVariable;
import net.amygdalum.comtemplate.engine.ValueDefinition;

public class ScopeTest {

	private TemplateDefinition definition;
	private List<TemplateVariable> variables;

	@BeforeEach
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
		assertThat(new Scope(definition, var("param", string("string"))).resolveVariable("param").get().getValue(), equalTo(string("string")));
	}

	@Test
	public void testResolveVariableToConstant() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		ValueDefinition definedConstant = group.defineConstant("constant");
		definedConstant.setValue(string("string"));
		definition = definedConstant;
		assertThat(new Scope(definition).resolveVariable("constant").get().getValue(), equalTo(string("string")));
	}

	@Test
	public void testResolveVariableInDistantDefinedScope() throws Exception {
		TemplateDefinition parentDefinition = new TestTemplateDefinition("parent");
		Scope parent = new Scope(parentDefinition, var("param", string("parentstring")));
		assertThat(new Scope(parent, definition, var("param", string("string"))).resolveVariable("param", parentDefinition).get().getValue(), equalTo(string("parentstring")));
	}

	@Test
	public void testResolveVariableInFailingDefinedScope() throws Exception {
		TemplateDefinition parentDefinition = new TestTemplateDefinition("parent");
		Scope parent = new Scope(parentDefinition, var("param", string("string")));
		assertThat(new Scope(parent, definition).resolveVariable("param", definition).isPresent(), is(false));
	}

	@Test
	public void testResolveVariableFailsWithoutDefinitionAndParent() throws Exception {
		assertThat(new Scope(null).resolveVariable("param", definition).isPresent(), is(false));
	}

	@Test
	public void testResolveContextVariable() throws Exception {
		assertThat(new Scope(definition, var("param", string("string"))).resolveContextVariable("param").get().getValue(), equalTo(string("string")));
	}

	@Test
	public void testResolveContextVariableInDistantDefinedScope() throws Exception {
		TemplateDefinition parentDefinition = new TestTemplateDefinition("parent");
		Scope parent = new Scope(parentDefinition, var("param", string("string")));
		assertThat(new Scope(parent, definition).resolveContextVariable("param").get().getValue(), equalTo(string("string")));
	}

	@Test
	public void testResolveContextVariableFails() throws Exception {
		TemplateDefinition parentDefinition = new TestTemplateDefinition("parent");
		Scope parent = new Scope(parentDefinition);
		assertThat(new Scope(parent, definition).resolveContextVariable("param").isPresent(), is(false));
	}

	@Test
	public void testResolveTemplateNoGroup() throws Exception {
		Scope scope = new Scope(definition, var("param", string("string")));
		assertThat(scope.resolveTemplate("call"), nullValue());
	}

	@Test
	public void testResolveTemplateGroupDefined() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		CustomTemplateDefinition calldef = group.defineTemplate("call");
		definition.setGroup(group);
		assertThat(new Scope(definition, var("param", string("string"))).resolveTemplate("call"), sameInstance(calldef));
	}

	@Test
	public void testResolveTemplateGroupImported() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		TestTemplateDefinition calldef = new TestTemplateDefinition("call");
		group.addImport(calldef);
		definition.setGroup(group);
		assertThat(new Scope(definition, var("param", string("string"))).resolveTemplate("call"), sameInstance(calldef));
	}

	@Test
	public void testResolveTemplateInDistantDefinedScope() throws Exception {
		TemplateGroup group = new TemplateGroup("group", "testresource");
		TemplateDefinition parentDefinition = group.defineTemplate("parent");
		TemplateDefinition tempDefinition = group.defineTemplate("temp");
		Scope parent = new Scope(parentDefinition);
		assertThat(new Scope(parent, definition).resolveTemplate("temp", parentDefinition), sameInstance(tempDefinition));
	}

	@Test
	public void testResolveTemplateInFailingDefinedScope() throws Exception {
		TemplateDefinition parentDefinition = new TestTemplateDefinition("parent");
		Scope parent = new Scope(parentDefinition);
		Scope scope = new Scope(parent, definition);
		assertThat(scope.resolveTemplate("temp", definition), nullValue());
	}

	@Test
	public void testResolveTemplateFailsWithoutDefinitionAndParent() throws Exception {
		assertThat(new Scope(null).resolveTemplate("temp", definition), nullValue());
	}

}
