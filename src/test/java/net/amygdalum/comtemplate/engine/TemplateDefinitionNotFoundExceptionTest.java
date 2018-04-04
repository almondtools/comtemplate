package net.amygdalum.comtemplate.engine;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class TemplateDefinitionNotFoundExceptionTest {

	@Test
	public void testGetMessage() throws Exception {
		TemplateDefinitionNotFoundException exception = new TemplateDefinitionNotFoundException("group", "template");
		
		assertThat(exception.getMessage(), equalTo("cannot find template definition <template> in <group>"));
	}

}
