package net.amygdalum.comtemplate.engine;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class ArgumentRequiredExceptionTest {

	@Test
	public void testGetMessage() throws Exception {
		ArgumentRequiredException exception = new ArgumentRequiredException("var");
		
		assertThat(exception.getMessage(), equalTo("argument <var> is required"));
	}

}
