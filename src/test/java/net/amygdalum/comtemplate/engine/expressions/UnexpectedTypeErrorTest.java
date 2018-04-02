package net.amygdalum.comtemplate.engine.expressions;

import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.expressions.UnexpectedTypeError;


public class UnexpectedTypeErrorTest {

	@Test
	public void testGetMessage() throws Exception {
		UnexpectedTypeError error = new UnexpectedTypeError("double", integer(12));
		
		assertThat(error.getMessage(), equalTo("expected expression evaluable to <double> but found <12>"));
	}

}
