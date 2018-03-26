package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.conmatch.conventions.EqualityMatcher.satisfiesDefaultEquality;
import static java.math.BigInteger.valueOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;


public class IntegerLiteralTest {

	@Test
	public void testGetValue() throws Exception {
		assertThat(integer(2).getValue(), equalTo(BigInteger.valueOf(2)));
	}

	@Test
	public void testGetText() throws Exception {
		assertThat(integer(42).getText(), equalTo("42"));
	}

	@Test
	public void testAsBigInteger() throws Exception {
		assertThat(integer(7).as(BigInteger.class), equalTo(BigInteger.valueOf(7)));
	}

	@Test
	public void testAsByte() throws Exception {
		assertThat(integer(7).as(Byte.class), equalTo(Byte.valueOf((byte) 7)));
	}
	
	@Test
	public void testAsShort() throws Exception {
		assertThat(integer(7).as(Short.class), equalTo(Short.valueOf((short) 7)));
	}
	
	@Test
	public void testAsInteger() throws Exception {
		assertThat(integer(7).as(Integer.class), equalTo(Integer.valueOf(7)));
	}
	
	@Test
	public void testAsLong() throws Exception {
		assertThat(integer(7).as(Long.class), equalTo(Long.valueOf(7)));
	}
	
	@Test
	public void testAsDefault() throws Exception {
		assertThat(integer(7).as(Void.class), nullValue());
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		
		integer(7).apply(visitor, scope);

		verify(visitor).visitIntegerLiteral(integer(7), scope);
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(integer(77), satisfiesDefaultEquality()
			.andEqualTo(integer(valueOf(77)))
			.andEqualTo(integer(77))
			.andEqualTo(integer("77"))
			.andNotEqualTo(integer(-77)));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(integer(23).toString(), equalTo("23"));
	}

}
