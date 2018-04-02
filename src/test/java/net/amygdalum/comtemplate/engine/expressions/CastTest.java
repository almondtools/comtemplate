package net.amygdalum.comtemplate.engine.expressions;

import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.expressions.Cast;


public class CastTest {

	@Test
	public void testGetExpression() throws Exception {
		assertThat(new Cast(integer(1), "object").getExpression(), equalTo(integer(1)));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		Cast concat = new Cast(integer(1), "object");

		concat.apply(visitor, scope);

		verify(visitor).visitCast(concat, scope);
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new Cast(integer(1), "object").toString(), equalTo("(object) 1"));
	}

}
