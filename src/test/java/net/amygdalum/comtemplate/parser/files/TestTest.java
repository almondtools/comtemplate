package net.amygdalum.comtemplate.parser.files;

import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static net.amygdalum.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static net.amygdalum.comtemplate.parser.files.TemplateTests.compileLibrary;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.amygdalum.comtemplate.engine.DefaultErrorHandler;
import net.amygdalum.comtemplate.engine.DefaultTemplateInterpreter;
import net.amygdalum.comtemplate.engine.TemplateGroup;
import net.amygdalum.comtemplate.engine.TemplateInterpreter;
import net.amygdalum.comtemplate.engine.TemplateLoader;
import net.amygdalum.comtemplate.engine.expressions.BooleanLiteral;

public class TestTest {

	private TemplateLoader loader;
	private TemplateGroup group;
	private TemplateInterpreter interpreter;

	@BeforeEach
	public void before() throws Exception {
		loader = Mockito.mock(TemplateLoader.class);
		group = compileLibrary("src/test/resources/test.ctp");
		interpreter = new DefaultTemplateInterpreter(loader, defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

	@Test
	public void testTestTrue() throws Exception {
		BooleanLiteral results = (BooleanLiteral) group.getDefinition("test").evaluate(interpreter, null, emptyList());
		assertThat(results.as(Boolean.class), is(true));
	}

	@Test
	public void testTestFalse() throws Exception {
		BooleanLiteral results = (BooleanLiteral) group.getDefinition("fail").evaluate(interpreter, null, emptyList());
		assertThat(results.as(Boolean.class), is(false));
	}

}
