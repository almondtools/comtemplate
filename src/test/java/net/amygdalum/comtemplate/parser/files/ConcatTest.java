package net.amygdalum.comtemplate.parser.files;

import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.TemplateGroup;

public class ConcatTest extends TemplateTests {

	private TemplateGroup group;

	@BeforeEach
	public void before() throws IOException {
		group = compileLibrary("src/test/resources/concat.ctp");
	}
	
	@Test
	public void testConcatLists() throws Exception {
		String rendered = group.getDefinition("concatLists").evaluate(interpreter());
		assertThat(rendered, equalTo("1 2 | 3 4"));
	}
	
	@Test
	public void testConcatMaps() throws Exception {
		String rendered = group.getDefinition("concatMaps").evaluate(interpreter());
		assertThat(rendered, equalTo("a b -> 1 2"));
	}

	
}
