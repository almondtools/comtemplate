package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.util.objects.EqualityMatcher.satisfiesDefaultEquality;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.Test;

import com.almondtools.comtemplate.engine.CustomTemplateDefinition;
import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;


public class RawTextTest {

	@Test
	public void testEvaluateWithConstantText() throws Exception {
		CustomTemplateDefinition definition = new CustomTemplateDefinition("name", emptyList());
		definition.add(new RawText("text"));

		String evaluated = definition.evaluate(Collections.emptyList());
		assertThat(evaluated, equalTo("text"));
	}

	@Test
	public void testGetText() throws Exception {
		assertThat(new RawText("string").getText(), equalTo("string"));
	}

	@Test
	public void testAsString() throws Exception {
		assertThat(new RawText("string").as(String.class), equalTo("string"));
	}

	@Test
	public void testAsDefault() throws Exception {
		assertThat(new RawText("string").as(Void.class), nullValue());
	}
	
	@Test
	public void testJoin() throws Exception {
		RawText rawText = new RawText("a");
		rawText.join(new RawText("b"));
		assertThat(rawText, equalTo(new RawText("ab")));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		RawText literal = new RawText("text");

		literal.apply(visitor, scope);

		verify(visitor).visitRawText(literal, scope);
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(new RawText("text"), satisfiesDefaultEquality()
			.andEqualTo(new RawText("text"))
			.andNotEqualTo(new RawText("corrupt text"))
			.andNotEqualTo(new RawText("no text")));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new RawText("string").toString(), equalTo("'string'"));
	}

}
