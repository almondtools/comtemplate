package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.ListLiteral.list;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class ParametersTest extends TemplateTests {

	private TemplateGroup group;

	@Before
	public void before() throws Exception {
		group = compileFile("src/test/resources/parameters.ctp");
	}

	@Test
	public void testParameterPassing() throws Exception {
		String rendered = group.getDefinition("html").evaluate(var("content", string("content")), var("attributes", list(string("lang=\"de\""))));
		assertThat(rendered, equalTo("  <html lang=\"de\">content</html>"));
	}

	@Test
	public void testParameterDefault() throws Exception {
		String rendered = group.getDefinition("html").evaluate();
		assertThat(rendered, equalTo("  <html></html>"));
	}

	@Test
	public void testArgumentsByName() throws Exception {
		String rendered = group.getDefinition("testByName").evaluate();
		assertThat(rendered, equalTo("  <html lang=\"de\">inhalt</html>"));
	}

	@Test
	public void testArgumentsBySequence() throws Exception {
		String rendered = group.getDefinition("testBySequence").evaluate();
		assertThat(rendered, equalTo("  <html lang=\"en\">content</html>"));
	}

}
