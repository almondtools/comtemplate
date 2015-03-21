package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class UnexpectedTypeErrorTest {

	@Test
	public void testGetMessage() throws Exception {
		UnexpectedTypeError error = new UnexpectedTypeError("double", integer(12));
		
		assertThat(error.getMessage(), equalTo("expected expression evaluable to <double> but found <12>"));
	}

}
