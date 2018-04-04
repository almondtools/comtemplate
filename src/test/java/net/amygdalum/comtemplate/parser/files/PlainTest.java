package net.amygdalum.comtemplate.parser.files;

import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.TemplateDefinition;

public class PlainTest {

	@Test
	public void testFile() throws Exception {
		TemplateDefinition plain = TemplateTests.compileMain("src/test/resources/plain.ctp");
		String rendered = plain.evaluate(interpreter(), var("name", string("My Name")));
		assertThat(rendered, equalTo("Hello My Name"));
	}

	@Test
	public void testString() throws Exception {
		TemplateDefinition plain = TemplateTests.compileMainFromText("Hello <<name>>");
		String rendered = plain.evaluate(interpreter(), var("name", string("Next Name")));
		assertThat(rendered, equalTo("Hello Next Name"));
	}

}
