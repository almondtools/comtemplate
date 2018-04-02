package net.amygdalum.comtemplate.engine.expressions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.expressions.EvalContextVar;


public class EvalContextVarTest {

	@Test
	public void testGetName() throws Exception {
		assertThat(new EvalContextVar("cvar").getName(), equalTo("cvar"));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		EvalContextVar eval = new EvalContextVar("cvar");

		eval.apply(visitor, scope);

		verify(visitor).visitEvalContextVar(eval, scope);
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new EvalContextVar("cvar").toString(), equalTo("@@cvar"));
	}

}
