package com.almondtools.comtemplate.engine;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TemplateGroupNotFoundExceptionTest {

	@Test
	public void testGetMessage() throws Exception {
		assertThat(new TemplateGroupNotFoundException("file").getMessage(), equalTo("cannot find template group <file>"));
	}

}
