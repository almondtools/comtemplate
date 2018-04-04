package net.amygdalum.comtemplate.parser.files;

import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static net.amygdalum.comtemplate.parser.files.TemplateTests.compileLibrary;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.TemplateGroup;

public class ForTest {

	private TemplateGroup group;

	@BeforeEach
	public void before() throws Exception {
		group = compileLibrary("src/test/resources/for.ctp");
	}
	
	@Test
	public void testForInDefault() throws Exception {
		String rendered = group.getDefinition("forInDefault").evaluate(interpreter());
		assertThat(rendered, equalTo("  :a:b"));
	}
	
	@Test
	public void testForInList() throws Exception {
		String rendered = group.getDefinition("forInList").evaluate(interpreter());
		assertThat(rendered, equalTo("   1:1 2:2"));
	}
	
	@Test
	public void testForInListI() throws Exception {
		String rendered = group.getDefinition("forInListI").evaluate(interpreter());
		assertThat(rendered, equalTo("   1:a 2:b"));
	}
	
	@Test
	public void testForInListI0() throws Exception {
		String rendered = group.getDefinition("forInListI0").evaluate(interpreter());
		assertThat(rendered, equalTo("   0:a 1:b"));
	}
	
}
