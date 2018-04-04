package net.amygdalum.comtemplate.engine.expressions;

import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;

public class EvalTemplateFunctionTest {

	@Test
	public void testGetTemplate() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplateFunction("template", definition).getTemplate(), equalTo("template"));
	}

	@Test
	public void testGetDefinition() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplateFunction("template", definition).getDefinition(), sameInstance(definition));
	}

	@Test
	public void testGetArguments() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplateFunction("template", definition, string("str")).getArguments(), contains(string("str")));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalTemplateFunction eval = new EvalTemplateFunction("template", definition);

		eval.apply(visitor, scope);

		verify(visitor).visitEvalTemplateFunction(eval, scope);
	}

	@Test
	public void testToString() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplateFunction("template", definition, string("a"),string("b")).toString(), equalTo("@template('a','b')"));
	}

}
