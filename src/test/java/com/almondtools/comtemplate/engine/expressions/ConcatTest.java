package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class ConcatTest {

	@Test
	public void testGetExpressions() throws Exception {
		assertThat(new Concat(string("a"), integer(1)).getExpressions(), contains(string("a"), integer(1)));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		Concat concat = new Concat(string("a"));

		concat.apply(visitor, scope);

		verify(visitor).visitConcat(concat, scope);
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new Concat(string("a"), integer(1)).toString(), equalTo("'a'~1"));
	}

}
