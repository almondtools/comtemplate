package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionErrors;


public class ExpressionResolutionErrorsTest {

	@Test
	public void testGetErrors() throws Exception {
		ExpressionResolutionError error1 = new ExpressionResolutionError(string("base"), "function1", asList(string("a"), string("b")), null, null);
		ExpressionResolutionError error2 = new ExpressionResolutionError(string("base"), "function2", asList(string("a"), string("b")), null, null);
		ExpressionResolutionErrors error = new ExpressionResolutionErrors(string("base"), "function", asList(string("a"), string("b")), null, null, asList(error1, error2));
		
		assertThat(error.getErrors(), contains(error1, error2));
	}
	
	@Test
	public void testGetErrorsEmpty() throws Exception {
		ExpressionResolutionErrors error = new ExpressionResolutionErrors(string("base"), "function", asList(string("a"), string("b")), null, null, emptyList());
		
		assertThat(error.getErrors(), empty());
	}

	@Test
	public void testGetErrorsFlat() throws Exception {
		ExpressionResolutionError error1 = new ExpressionResolutionError(string("base"), "function1", asList(string("a"), string("b")), null, null);
		ExpressionResolutionError error2 = new ExpressionResolutionError(string("base"), "function2", asList(string("a"), string("b")), null, null);
		ExpressionResolutionErrors nestederror = new ExpressionResolutionErrors(string("base"), "function", asList(string("a"), string("b")), null, null, asList(error1, error2));
		ExpressionResolutionErrors error = new ExpressionResolutionErrors(string("base"), "function", asList(string("a"), string("b")), null, null, asList(nestederror));
		
		assertThat(error.getErrors(), contains(error1, error2));
	}
}
