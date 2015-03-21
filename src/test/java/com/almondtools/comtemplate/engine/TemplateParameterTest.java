package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateParameter.param;
import static com.almondtools.comtemplate.engine.TemplateParameter.toParams;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static com.almondtools.util.objects.EqualityMatcher.satisfiesDefaultEquality;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TemplateParameterTest {

	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Test
	public void testToParams() throws Exception {
		assertThat(toParams("a", "b"), contains(param("a"), param("b")));
	}

	@Test
	public void testToParamsMixed() throws Exception {
		assertThat(toParams("a", param("b", integer(2))), contains(param("a"), param("b", integer(2))));
	}

	@Test
	public void testToParamsFails() throws Exception {
		expected.expect(IllegalArgumentException.class);

		toParams(new Object());
	}

	@Test
	public void testGetName() throws Exception {
		assertThat(param("name").getName(), equalTo("name"));
	}

	@Test
	public void testGetType() throws Exception {
		assertThat(param("name", "type", null).getType(), equalTo("type"));
	}

	@Test
	public void testGetDefaultValue() throws Exception {
		assertThat(param("name", string("default")).getDefaultValue(), equalTo(string("default")));
	}

	@Test
	public void testGetDefaultValueNotExisting() throws Exception {
		assertThat(param("name").getDefaultValue(), nullValue());
	}

	@Test
	public void testFromMatches() throws Exception {
		assertThat(param("name").from(asList(var("name", string("var")))), equalTo(var("name", string("var"))));
	}

	@Test
	public void testFromFails() throws Exception {
		assertThat(param("name").from(asList(var("othername", string("var")))), nullValue());
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(param("name", decimal(2.2)), satisfiesDefaultEquality()
			.andEqualTo(param("name", decimal(2.2)))
			.andNotEqualTo(param("name"))
			.andNotEqualTo(param("", decimal(2.2)))
			.andNotEqualTo(param("name", decimal(2.3))));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(param("name", decimal(2.2)).toString(), equalTo("name=>2.2"));
	}

}
