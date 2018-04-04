package net.amygdalum.comtemplate.engine;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class TemplateGroupNotFoundExceptionTest {

	@Test
	public void testGetMessage() throws Exception {
		TemplateGroupNotFoundException exception = new TemplateGroupNotFoundException("file");
		
		assertThat(exception.getMessage(), equalTo("cannot find template group <file>"));
	}

	@Test
	public void testGetMessageWithFile() throws Exception {
		TemplateGroupNotFoundException exception = new TemplateGroupNotFoundException("file");
		exception.setFileName("affected.file");
		
		assertThat(exception.getMessage(), equalTo("cannot find template group <file> in file 'affected.file'"));
	}
	
}
