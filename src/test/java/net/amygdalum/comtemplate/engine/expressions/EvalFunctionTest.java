package net.amygdalum.comtemplate.engine.expressions;

import static net.amygdalum.comtemplate.engine.expressions.ListLiteral.list;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.expressions.EvalFunction;


public class EvalFunctionTest {

	@Test
	public void testGetBase() throws Exception {
		assertThat(new EvalFunction(string("text"), "toString").getBase(), equalTo(string("text")));
	}

	@Test
	public void testGetFunction() throws Exception {
		assertThat(new EvalFunction(string("text"), "toString").getFunction(), equalTo("toString"));
	}

	@Test
	public void testGetArguments() throws Exception {
		assertThat(new EvalFunction(string("text"), "matches",string("\\w+")).getArguments(), contains(string("\\w+")));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		EvalFunction eval = new EvalFunction(string("s"), "trim");

		eval.apply(visitor, scope);

		verify(visitor).visitEvalFunction(eval, scope);
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new EvalFunction(list(string("s"),string("t")), "separated", string(",")).toString(), equalTo("['s','t'].separated(',')"));
	}

}
