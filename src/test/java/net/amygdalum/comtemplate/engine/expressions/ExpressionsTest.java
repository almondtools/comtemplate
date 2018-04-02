package net.amygdalum.comtemplate.engine.expressions;

import static com.almondtools.conmatch.conventions.UtilityClassMatcher.isUtilityClass;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static net.amygdalum.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static net.amygdalum.comtemplate.engine.expressions.Expressions.fromNative;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.expressions.Expressions;
import net.amygdalum.comtemplate.engine.expressions.NativeObject;

public class ExpressionsTest {

	@Test
	public void testFromNativeTemplateExpression() throws Exception {
		assertThat(fromNative(string("")), equalTo(string("")));
	}

	@Test
	public void testFromNativeString() throws Exception {
		assertThat(fromNative("string"), equalTo(string("string")));
	}

	@Test
	public void testFromNativeChar() throws Exception {
		assertThat(fromNative('c'), equalTo(string("c")));
	}

	@Test
	public void testFromNativeBoolean() throws Exception {
		assertThat(fromNative(true), equalTo(TRUE));
	}

	@Test
	public void testFromNativeBigInteger() throws Exception {
		assertThat(fromNative(BigInteger.valueOf(12)), equalTo(integer(12)));
	}

	@Test
	public void testFromNativeBigDecimal() throws Exception {
		assertThat(fromNative(BigDecimal.valueOf(-2, 2)), equalTo(decimal(-0.02)));
	}

	@Test
	public void testFromNativeFloat() throws Exception {
		assertThat(fromNative(0.3f), equalTo(decimal(0.3)));
	}

	@Test
	public void testFromNativeDouble() throws Exception {
		assertThat(fromNative(0.3d), equalTo(decimal(0.3)));
	}

	@Test
	public void testFromNativeByte() throws Exception {
		assertThat(fromNative((byte) 9), equalTo(integer(9)));
	}

	@Test
	public void testFromNativeShort() throws Exception {
		assertThat(fromNative((short) 9), equalTo(integer(9)));
	}

	@Test
	public void testFromNativeInt() throws Exception {
		assertThat(fromNative(9), equalTo(integer(9)));
	}

	@Test
	public void testFromNativeLong() throws Exception {
		assertThat(fromNative(9l), equalTo(integer(9)));
	}

	@Test
	public void testFromNativeDefault() throws Exception {
		Object value = new Object();
		assertThat(fromNative(value), equalTo(new NativeObject(value)));
	}

	@Test
	public void testExpressions() throws Exception {
		assertThat(Expressions.class, isUtilityClass());
	}

}
