package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class ExistsTest {

	@Test
	public void testGetExpression() throws Exception {
		assertThat(new Exists(string("value")).getExpression(), equalTo(string("value")));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		Exists exists = new Exists(string("value"));
		
		exists.apply(visitor, scope);
		
		verify(visitor).visitExists(exists, scope);
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new Exists(string("value")).toString(), equalTo("'value'?"));
	}

}
