package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.expressions.ListLiteral.list;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class EvalVirtualTest {

	@Test
	public void testGetBase() throws Exception {
		assertThat(new EvalVirtual(new NativeObject("str"), string("att")).getBase(), equalTo(new NativeObject("str")));
	}

	@Test
	public void testGetAttribute() throws Exception {
		assertThat(new EvalVirtual(new NativeObject("str"), string("att")).getAttribute(), equalTo(string("att")));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		EvalVirtual eval = new EvalVirtual(list(string("str")), string("length"));

		eval.apply(visitor, scope);

		verify(visitor).visitEvalVirtual(eval, scope);
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new EvalVirtual(string("str"), string("length")).toString(), equalTo("'str'.('length')"));
	}

}
