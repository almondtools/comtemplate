package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.TestTemplateDefinition;

import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.expressions.TemplateResolutionError;


public class TemplateResolutionErrorTest {

	@Test
	public void testGetMessageBase() throws Exception {
		TemplateResolutionError error = new TemplateResolutionError("template", (TemplateDefinition) null);
		
		assertThat(error.getMessage(), equalTo("template <template> cannot be resolved"));
	}

	@Test
	public void testGetMessageDefinition() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class, RETURNS_DEEP_STUBS);
		when(definition.getLocation()).thenReturn("file:definer");
		when(definition.getGroup().getDefinitions()).thenReturn(asList(new TestTemplateDefinition("test1"), new TestTemplateDefinition("test2")));
		
		TemplateResolutionError error = new TemplateResolutionError("template", definition);
		String message = error.getMessage();
		
		assertThat(message, containsString("accessed in <file:definer>"));
		assertThat(message, containsString("available templates:"));
		assertThat(message, containsString("test1,test2"));
	}
	
}
