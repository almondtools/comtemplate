package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.conmatch.conventions.EqualityMatcher.satisfiesDefaultEquality;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class NativeObjectTest {

	@Test
	public void testGetValue() throws Exception {
		assertThat(new NativeObject(new JavaBean()).getObject(), instanceOf(JavaBean.class));
	}

	@Test
	public void testGetText() throws Exception {
		assertThat(new NativeObject(new JavaBean()).getText(), equalTo("java bean"));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		NativeObject literal = new NativeObject(new JavaBean());

		literal.apply(visitor, scope);

		verify(visitor).visitNativeObject(literal, scope);
	}

	@Test
	public void testAsMatch() throws Exception {
		JavaBean object = new JavaBean();
		assertThat(new NativeObject(object).as(JavaBean.class), sameInstance(object));
	}

	@Test
	public void testAsDefault() throws Exception {
		JavaBean object = new JavaBean();
		assertThat(new NativeObject(object).as(Void.class), nullValue());
	}

	@Test
	public void testEquals() throws Exception {
		JavaBean object = new JavaBean();
		assertThat(new NativeObject(object), satisfiesDefaultEquality()
			.andEqualTo(new NativeObject(object))
			.andNotEqualTo(new NativeObject(null))
			.andNotEqualTo(new NativeObject(new Object())));
		assertThat(new NativeObject(null), satisfiesDefaultEquality()
			.andEqualTo(new NativeObject(null)));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new NativeObject(new JavaBean()).toString(), equalTo("*'java bean'"));
	}

	private static class JavaBean {
		@Override
		public String toString() {
			return "java bean";
		}
	}

}
