package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.conmatch.conventions.EqualityMatcher.satisfiesDefaultEquality;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class TemplateVariableTest {

	@Test
	public void testGetName() throws Exception {
		assertThat(var("a", integer(2)).getName(), equalTo("a"));
	}

	@Test
	public void testHasName() throws Exception {
		assertThat(var("a", integer(2)).hasName("a"), is(true));
		assertThat(var("b", integer(2)).hasName("a"), is(false));
	}

	@Test
	public void testGetValue() throws Exception {
		assertThat(var("a", integer(2)).getValue(), equalTo(integer(2)));
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(var("name", decimal(2.2)), satisfiesDefaultEquality()
			.andEqualTo(var("name", decimal(2.2)))
			.andNotEqualTo(var("", decimal(2.2)))
			.andNotEqualTo(var("name", decimal(2.3))));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(var("name", decimal(-2.2)).toString(), equalTo("name=-2.2"));
	}

}
