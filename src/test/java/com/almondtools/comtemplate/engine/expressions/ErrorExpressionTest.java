package com.almondtools.comtemplate.engine.expressions;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;


public class ErrorExpressionTest {

	private ErrorExpression expression;
	
	@Before
	public void before() throws Exception {
		expression = new ErrorExpression() {
			
			@Override
			public String getMessage() {
				return "";
			}
		};
	}
	
	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		
		expression.apply(visitor, scope);
		
		verify(visitor).visitErrorExpression(expression, scope);
	}

	@Test
	public void testAsDefault() throws Exception {
		assertThat(expression.as(Void.class), nullValue());
	}

	@Test
	public void testGetText() throws Exception {
		assertThat(expression.getText(), isEmptyString());
	}

}
