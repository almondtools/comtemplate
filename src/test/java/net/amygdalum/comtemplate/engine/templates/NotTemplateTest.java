package net.amygdalum.comtemplate.engine.templates;

import static net.amygdalum.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static net.amygdalum.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.amygdalum.comtemplate.engine.DefaultErrorHandler;
import net.amygdalum.comtemplate.engine.SilentTemplateInterpreter;
import net.amygdalum.comtemplate.engine.TemplateInterpreter;
import net.amygdalum.comtemplate.engine.TemplateLoader;

public class NotTemplateTest {

	private TemplateLoader loader;
	private TemplateInterpreter interpreter;

	@BeforeEach
	public void before() throws Exception {
		loader = Mockito.mock(TemplateLoader.class);
		interpreter = new SilentTemplateInterpreter(loader, defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

	@Test
	public void testEvaluateTrue() throws Exception {
		NotTemplate template = new NotTemplate();
		String result = template.evaluate(interpreter, var("cond", TRUE));
		assertThat(result, equalTo("false"));
	}

	@Test
	public void testEvaluateFalse() throws Exception {
		NotTemplate template = new NotTemplate();
		String result = template.evaluate(interpreter, var("cond", FALSE));
		assertThat(result, equalTo("true"));
	}

	@Test
	public void testEvaluateCondIsNotBoolean() throws Exception {
		NotTemplate template = new NotTemplate();

		String result = template.evaluate(interpreter, var("cond", string("not boolean")));

		assertThat(result, equalTo(""));
	}

}
