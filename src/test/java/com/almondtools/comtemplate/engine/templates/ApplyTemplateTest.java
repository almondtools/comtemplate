package com.almondtools.comtemplate.engine.templates;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.MapLiteral.map;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.almondtools.comtemplate.engine.ArgumentRequiredException;
import com.almondtools.comtemplate.engine.DefaultErrorHandler;
import com.almondtools.comtemplate.engine.DefaultTemplateInterpreter;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.TemplateLoader;
import com.almondtools.comtemplate.engine.TestTemplateDefinition;
import com.almondtools.comtemplate.engine.expressions.BooleanLiteral;
import com.almondtools.comtemplate.engine.expressions.TemplateResolutionError;
import com.almondtools.comtemplate.engine.expressions.UnexpectedTypeError;

public class ApplyTemplateTest {

	private TemplateLoader loader;
	private TemplateInterpreter interpreter;

	@BeforeEach
	public void before() throws Exception {
		loader = Mockito.mock(TemplateLoader.class);
		interpreter = new DefaultTemplateInterpreter(loader, defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

	@Test
	public void testEvaluateSimple() throws Exception {
		ApplyTemplate template = new ApplyTemplate();
		Scope scope = mock(Scope.class);
		when(scope.resolveTemplate("template")).thenReturn(new TestTemplateDefinition("template", emptyList()));

		String result = template.evaluate(interpreter, scope, asList(var("name", string("template")))).getText();

		assertThat(result, equalTo("test: name='template'"));
	}

	@Test
	public void testEvaluateWithArguments() throws Exception {
		ApplyTemplate template = new ApplyTemplate();
		Scope scope = mock(Scope.class);
		when(scope.resolveTemplate("template")).thenReturn(new TestTemplateDefinition("template"));

		String result = template.evaluate(interpreter, scope, asList(var("name", string("template")), var("arguments", map(var("argument", string("value")))))).getText();

		assertThat(result, equalTo("test: argument='value'"));
	}

	@Test
	public void testEvaluateNoName() throws Exception {
		ApplyTemplate template = new ApplyTemplate();
		Scope scope = mock(Scope.class);
		assertThrows(ArgumentRequiredException.class, () -> template.evaluate(interpreter, scope, emptyList()).getText());
	}

	@Test
	public void testEvaluateNoStringName() throws Exception {
		ApplyTemplate template = new ApplyTemplate();
		Scope scope = mock(Scope.class);

		TemplateImmediateExpression result = template.evaluate(interpreter, scope, asList(var("name", BooleanLiteral.TRUE)));

		assertThat(result, instanceOf(UnexpectedTypeError.class));
	}

	@Test
	public void testEvaluateNoParent() throws Exception {
		ApplyTemplate template = new ApplyTemplate();

		TemplateImmediateExpression result = template.evaluate(interpreter, null, asList(var("name", string("template"))));

		assertThat(result, instanceOf(TemplateResolutionError.class));
	}

	@Test
	public void testEvaluateNoTemplateToResolve() throws Exception {
		ApplyTemplate template = new ApplyTemplate();

		Scope scope = mock(Scope.class);

		TemplateImmediateExpression result = template.evaluate(interpreter, scope, asList(var("name", string("template"))));

		assertThat(result, instanceOf(TemplateResolutionError.class));
	}

}
