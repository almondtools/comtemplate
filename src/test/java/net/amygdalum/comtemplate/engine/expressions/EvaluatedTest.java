package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;

public class EvaluatedTest {

	@Test
	public void testGetEvaluated() throws Exception {
		assertThat(new Evaluated(string("a"), string("b")).getEvaluated(), contains(string("a"), string("b")));
	}

	@Test
	public void testGetText() throws Exception {
		assertThat(new Evaluated(string("a"), string("b")).getText(), equalTo("ab"));
	}

	@Test
	public void testAsDefault() throws Exception {
		assertThat(new Evaluated(string("a"), string("b")).as(Void.class), nullValue());
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		Evaluated eval = new Evaluated(string("a"), string("b"));

		eval.apply(visitor, scope);

		verify(visitor).visitEvaluated(eval, scope);
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new Evaluated(string("a"), string("b")).toString(), equalTo("{'a','b'}"));
	}

	@Test
	public void testAssembling() throws Exception {
		assertThat(Evaluated.assembling().combiner().apply(asList(string("a")), asList(string("b"))), contains(string("a"), string("b")));
	}

}
