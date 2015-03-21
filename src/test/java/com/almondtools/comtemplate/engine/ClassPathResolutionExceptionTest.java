package com.almondtools.comtemplate.engine;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class ClassPathResolutionExceptionTest {

	@Test
	public void testGetMessage() throws Exception {
		assertThat(new ClassPathResolutionException("dir").getMessage(), equalTo("cannot find class path <dir>"));
	}

}
