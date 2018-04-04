package net.amygdalum.comtemplate.engine;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;


public class ContextRequiredExceptionTest {

	@Test
	public void testGetMessage() throws Exception {
		ContextRequiredException exception = new ContextRequiredException("var");
		
		assertThat(exception.getMessage(), equalTo("context variable <var> is required"));
	}

}
