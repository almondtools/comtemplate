package net.amygdalum.comtemplate.engine.expressions;

import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.MapLiteral.map;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;

public class ToObjectTest {

	@Test
	public void testGetType() throws Exception {
		assertThat(new ToObject("object", map(var("name", string("text")))).getType(), equalTo("object"));
	}

	@Test
	public void testGetExpression() throws Exception {
		assertThat(new ToObject("object", map(var("name", string("text")))).getExpression(), equalTo(map(var("name", string("text")))));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		ToObject toObject = new ToObject("object", string("value"));

		toObject.apply(visitor, scope);

		verify(visitor).visitToObject(toObject, scope);
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new ToObject("object", string("value")).toString(), equalTo("object('value')"));
	}

}
