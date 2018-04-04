package net.amygdalum.comtemplate.parser.files;

import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static net.amygdalum.comtemplate.parser.files.TemplateTests.compileLibrary;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.TemplateGroup;

public class ApplyTest {

	private TemplateGroup group;

	@BeforeEach
	public void before() throws Exception {
		group = compileLibrary("src/test/resources/apply.ctp");
	}

	@Test
	public void testApplyObject() throws Exception {
		String rendered = group.getDefinition("applyObject").evaluate(interpreter());
		assertThat(rendered, equalTo("myname"));
	}

	@Test
	public void testApplyTemplate() throws Exception {
		String rendered = group.getDefinition("applyTemplate").evaluate(interpreter());
		assertThat(rendered, equalTo("mycontent"));
	}
}
