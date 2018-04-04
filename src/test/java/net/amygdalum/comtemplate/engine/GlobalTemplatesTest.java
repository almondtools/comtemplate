package net.amygdalum.comtemplate.engine;

import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.templates.ForTemplate;
import net.amygdalum.comtemplate.engine.templates.IfTemplate;


public class GlobalTemplatesTest {

	@Test
	public void testDefaultTemplates() throws Exception {
		GlobalTemplates global = GlobalTemplates.defaultTemplates();
		assertThat(global.resolve("if"), instanceOf(IfTemplate.class));
		assertThat(global.resolve("for"), instanceOf(ForTemplate.class));
	}

	@Test
	public void testRegisterResolve() throws Exception {
		GlobalTemplates global = new GlobalTemplates();
		TestTemplateDefinition def = new TestTemplateDefinition("registeredName");
		global.register(def);
		assertThat(global.resolve("registeredName"), equalTo(def));
	}

	@Test
	public void testRegisterResolveGlobal() throws Exception {
		GlobalTemplates global = new GlobalTemplates();
		global.register("global", string("string"));
		assertThat(global.resolveGlobal("global").get(), equalTo(var("global",string("string"))));
	}

}
