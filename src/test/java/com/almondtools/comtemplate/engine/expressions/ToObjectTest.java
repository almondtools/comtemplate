package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.MapLiteral.map;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

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
