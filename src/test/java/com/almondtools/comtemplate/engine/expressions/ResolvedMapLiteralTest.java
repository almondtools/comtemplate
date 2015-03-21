package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static com.almondtools.util.objects.EqualityMatcher.satisfiesDefaultEquality;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class ResolvedMapLiteralTest {

	@Test
	public void testGetAttribute() throws Exception {
		assertThat(new ResolvedMapLiteral(var("key", string("value"))).getAttribute("key"), equalTo(string("value")));
		assertThat(new ResolvedMapLiteral(var("key", string("value"))).getAttribute("undefinedfield"), nullValue());
	}

	@Test
	public void testGetMap() throws Exception {
		assertThat(new ResolvedMapLiteral(var("key1", string("value1")), var("key2", string("value2"))).getMap().keySet(), containsInAnyOrder("key1", "key2"));
		assertThat(new ResolvedMapLiteral(var("key1", string("value1")), var("key2", string("value2"))).getMap().values(), containsInAnyOrder(string("value1"), string("value2")));
	}

	@Test
	public void testGetText() throws Exception {
		assertThat(new ResolvedMapLiteral(var("key1", string("value1")), var("key2", string("value2"))).getText(), equalTo("[key1=value1, key2=value2]"));
	}

	@Test
	public void testAs() throws Exception {
		assertThat(new ResolvedMapLiteral(var("key1", string("value1"))).as(Void.class), nullValue());
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		ResolvedMapLiteral literal = new ResolvedMapLiteral(var("a", string("a")));

		literal.apply(visitor, scope);

		verify(visitor).visitMapLiteral(literal, scope);
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(new ResolvedMapLiteral(var("a", integer(1))), satisfiesDefaultEquality()
			.andEqualTo(new ResolvedMapLiteral(var("a", integer(1))))
			.andEqualTo(new ResolvedMapLiteral(var("a", integer(0)), var("a", integer(1))))
			.andNotEqualTo(new ResolvedMapLiteral(var("a", integer(1)), var("a", integer(0))))
			.andNotEqualTo(new ResolvedMapLiteral()));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new ResolvedMapLiteral(var("a", string("a"))).toString(), equalTo("[a='a']"));
	}

}
