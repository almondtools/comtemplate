package net.amygdalum.comtemplate.engine;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import net.amygdalum.comtemplate.engine.expressions.TestError;

import net.amygdalum.comtemplate.engine.DefaultErrorHandler;
import net.amygdalum.util.extension.LogBackExtension;
import net.amygdalum.util.extension.LogBackLog;
import net.amygdalum.util.extension.LogBackExtension.ForClass;


@ExtendWith(LogBackExtension.class)
public class DefaultErrorHandlerTest {

	@Test
	public void testHandle(@ForClass(DefaultErrorHandler.class)LogBackLog log) throws Exception {
		new DefaultErrorHandler().handle(new TestError("test error"));
		assertThat(log.getError(), containsString("test error"));
	}

}
