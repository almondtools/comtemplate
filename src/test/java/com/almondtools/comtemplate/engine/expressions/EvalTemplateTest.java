package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class EvalTemplateTest {

	@Test
	public void testGetTemplate() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplate("template", definition).getTemplate(), equalTo("template"));
	}

	@Test
	public void testGetDefinition() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplate("template", definition).getDefinition(), sameInstance(definition));
	}

	@Test
	public void testGetArguments() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplate("template", definition, var("s", string("str"))).getArguments(), contains(var("s", string("str"))));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalTemplate eval = new EvalTemplate("template", definition);

		eval.apply(visitor, scope);

		verify(visitor).visitEvalTemplate(eval, scope);
	}

	@Test
	public void testToString() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplate("template", definition, var("a", string("a")), var("b", string("b"))).toString(), equalTo("@template(a='a',b='b')"));
	}

}
