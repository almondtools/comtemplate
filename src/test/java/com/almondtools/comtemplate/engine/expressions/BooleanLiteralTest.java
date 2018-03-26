package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.bool;
import static com.almondtools.conmatch.conventions.EqualityMatcher.satisfiesDefaultEquality;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class BooleanLiteralTest {

	@Test
	public void testBoolFailsOnNull() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> bool((Boolean) null));
	}

	@Test
	public void testGetValue() throws Exception {
		assertThat(TRUE.getValue(), equalTo(Boolean.TRUE));
	}

	@Test
	public void testGetText() throws Exception {
		assertThat(TRUE.getText(), equalTo("true"));
		assertThat(FALSE.getText(), equalTo("false"));
	}

	@Test
	public void testInvert() throws Exception {
		assertThat(TRUE.invert(), equalTo(FALSE));
		assertThat(FALSE.invert(), equalTo(TRUE));
	}

	@Test
	public void testAsBoolean() throws Exception {
		assertThat(TRUE.as(Boolean.class), is(true));
		assertThat(FALSE.as(Boolean.class), is(false));
	}

	@Test
	public void testAsDefault() throws Exception {
		assertThat(TRUE.as(Void.class), nullValue());
		assertThat(FALSE.as(Void.class), nullValue());
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		TRUE.apply(visitor, scope);
		verify(visitor).visitBooleanLiteral(TRUE, scope);
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(TRUE, satisfiesDefaultEquality()
			.andEqualTo(bool(true))
			.andEqualTo(bool("true"))
			.andNotEqualTo(FALSE));
		assertThat(FALSE, satisfiesDefaultEquality()
			.andEqualTo(bool(false))
			.andEqualTo(bool("false"))
			.andNotEqualTo(TRUE));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(TRUE.toString(), equalTo("true"));
		assertThat(FALSE.toString(), equalTo("false"));
	}

}
