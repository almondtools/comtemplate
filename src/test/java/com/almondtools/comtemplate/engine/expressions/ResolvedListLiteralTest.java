package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static com.almondtools.util.objects.EqualityMatcher.satisfiesDefaultEquality;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;


public class ResolvedListLiteralTest {

	@Test
	public void testGetElement() throws Exception {
		assertThat(new ResolvedListLiteral(string("value")).getElement(0), equalTo(string("value")));
		assertThat(new ResolvedListLiteral(string("value")).getElement(1), nullValue());
		assertThat(new ResolvedListLiteral(string("value")).getElement(-1), nullValue());
	}

	@Test
	public void testGetList() throws Exception {
		assertThat(new ResolvedListLiteral(string("value1"), string("value2")).getList(), contains(string("value1"), string("value2")));
	}

	@Test
	public void testGetText() throws Exception {
		assertThat(new ResolvedListLiteral(string("value1"),string("value2")).getText(), equalTo("[value1, value2]"));
	}

	@Test
	public void testAs() throws Exception {
		assertThat(new ResolvedListLiteral(string("value1"),string("value2")).as(Void.class), nullValue());
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		ResolvedListLiteral literal = new ResolvedListLiteral(string("a"), string("b"));

		literal.apply(visitor, scope);

		verify(visitor).visitListLiteral(literal, scope);
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(new ResolvedListLiteral(string("value1"), string("value2")), satisfiesDefaultEquality()
			.andEqualTo(new ResolvedListLiteral(string("value1"), string("value2")))
			.andNotEqualTo(new ResolvedListLiteral(string("value1")))
			.andNotEqualTo(new ResolvedListLiteral(string("value2"))));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new ResolvedListLiteral(string("a"), string("b")).toString(), equalTo("['a','b']"));
	}

}
