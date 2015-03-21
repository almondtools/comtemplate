package com.almondtools.comtemplate.engine;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.almondtools.comtemplate.engine.expressions.TestError;
import com.almondtools.util.logback.LogBackLog;


public class DefaultErrorHandlerTest {

	@Rule
	public LogBackLog log = LogBackLog.forClass(DefaultErrorHandler.class);
	 
	@Test
	public void testHandle() throws Exception {
		new DefaultErrorHandler().handle(new TestError("test error"));
		assertThat(log.getError(), containsString("test error"));
	}

}
