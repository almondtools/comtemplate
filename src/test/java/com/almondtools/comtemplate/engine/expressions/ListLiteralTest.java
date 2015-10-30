package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.ListLiteral.list;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static com.almondtools.conmatch.conventions.EqualityMatcher.satisfiesDefaultEquality;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class ListLiteralTest {

	@Test
	public void testGetList() throws Exception {
		ListLiteral literal = list(integer(666));
		assertThat(literal.getList(), contains(integer(666)));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		ListLiteral literal = list(string("a"), string("b"));

		literal.apply(visitor, scope);

		verify(visitor).visitListLiteral(literal, scope);
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(list(string("a"), string("b")), satisfiesDefaultEquality()
			.andEqualTo(list(string("a"), string("b")))
			.andNotEqualTo(list(string("a"))));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(list(string("a"), string("b")).toString(), equalTo("['a','b']"));
	}

}
