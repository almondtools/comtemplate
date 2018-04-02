package net.amygdalum.comtemplate.engine;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.TemplateGroupNotFoundException;

public class TemplateGroupNotFoundExceptionTest {

	@Test
	public void testGetMessage() throws Exception {
		assertThat(new TemplateGroupNotFoundException("file").getMessage(), equalTo("cannot find template group <file>"));
	}

}
