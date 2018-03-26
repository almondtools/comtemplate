package com.almondtools.comtemplate.engine;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.almondtools.comtemplate.engine.expressions.TestError;
import com.almondtools.util.extension.LogBackExtension;
import com.almondtools.util.extension.LogBackExtension.ForClass;
import com.almondtools.util.extension.LogBackLog;


@ExtendWith(LogBackExtension.class)
public class DefaultErrorHandlerTest {

	@Test
	public void testHandle(@ForClass(DefaultErrorHandler.class)LogBackLog log) throws Exception {
		new DefaultErrorHandler().handle(new TestError("test error"));
		assertThat(log.getError(), containsString("test error"));
	}

}
