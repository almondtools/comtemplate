package net.amygdalum.comtemplate.engine;

import static net.amygdalum.comtemplate.engine.Messages.setERROR;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.amygdalum.comtemplate.engine.expressions.TestError;


public class DefaultErrorHandlerTest {

	@Test
	public void testHandle() throws Exception {
		Messages messages = Mockito.mock(Messages.class);
		setERROR(messages);
		
		new DefaultErrorHandler().handle(new TestError("test error"));
		
		Mockito.verify(messages).log("test error");
	}

}
