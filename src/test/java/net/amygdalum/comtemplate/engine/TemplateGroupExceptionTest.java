package net.amygdalum.comtemplate.engine;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;


public class TemplateGroupExceptionTest {

	@Test
	public void testGetMessage() throws Exception {
		TemplateGroupException exception = new TemplateGroupException("file", asList("message1", "message2"));
		
		assertThat(exception.getMessage(), equalTo("parsing template group <file> failed:\n- message1\n- message2"));
	}
	
	@Test
	public void testGetMessageWithFile() throws Exception {
		TemplateGroupException exception = new TemplateGroupException("file", asList("message1", "message2"));
		exception.setFileName("affected.file");
		
		assertThat(exception.getMessage(), equalTo("parsing template group <file> in file 'affected.file' failed:\n- message1\n- message2"));
	}
	
}
