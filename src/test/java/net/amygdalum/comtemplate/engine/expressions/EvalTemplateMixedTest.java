package net.amygdalum.comtemplate.engine.expressions;

import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.expressions.EvalTemplateMixed;

public class EvalTemplateMixedTest {

	@Test
	public void testGetTemplate() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplateMixed("template", definition, new TemplateExpression[0]).getTemplate(), equalTo("template"));
	}

	@Test
	public void testGetDefinition() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplateMixed("template", definition, new TemplateExpression[0]).getDefinition(), sameInstance(definition));
	}

	@Test
	public void testGetArguments() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplateMixed("template", definition, new TemplateExpression[] { string("str") }, var("named", string("namedstr"))).getArguments(), contains(string("str")));
	}

	@Test
	public void testGetNamedArguments() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplateMixed("template", definition, new TemplateExpression[] { string("str") }, var("named", string("namedstr"))).getNamedArguments(),
			contains(var("named", string("namedstr"))));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		TemplateDefinition definition = mock(TemplateDefinition.class);
		EvalTemplateMixed eval = new EvalTemplateMixed("template", definition, new TemplateExpression[0]);

		eval.apply(visitor, scope);

		verify(visitor).visitEvalTemplateMixed(eval, scope);
	}

	@Test
	public void testToString() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		assertThat(new EvalTemplateMixed("template", definition, new TemplateExpression[] { string("a"), string("b") }).toString(), equalTo("@template('a','b')"));
		assertThat(new EvalTemplateMixed("template", definition, new TemplateExpression[0], var("a", string("b"))).toString(), equalTo("@template(a='b')"));
		assertThat(new EvalTemplateMixed("template", definition, new TemplateExpression[] { string("a") }, var("b", string("c"))).toString(), equalTo("@template('a',b='c')"));
	}

}
