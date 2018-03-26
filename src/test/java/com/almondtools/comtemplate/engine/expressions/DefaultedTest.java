package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class DefaultedTest {

	@Test
	public void testGetExpression() throws Exception {
		assertThat(new Defaulted(string("value"), string("default")).getExpression(), equalTo(string("value")));
	}

	@Test
	public void testGetDefaultExpression() throws Exception {
		assertThat(new Defaulted(string("value"), string("default")).getDefaultExpression(), equalTo(string("default")));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		Defaulted defaulted = new Defaulted(string("value"), string("default"));

		defaulted.apply(visitor, scope);

		verify(visitor).visitDefaulted(defaulted, scope);
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new Defaulted(string("value"), string("default")).toString(), equalTo("'value'?:'default'"));
	}

}
