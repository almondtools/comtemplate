package com.almondtools.comtemplate.engine.expressions;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateDefinition;


public class VariableResolutionErrorTest {

	@Test
	public void testGetMessageBase() throws Exception {
		VariableResolutionError error = new VariableResolutionError("var", (TemplateDefinition) null);
		
		assertThat(error.getMessage(), equalTo("variable <var> cannot be resolved"));
	}

	@Test
	public void testGetMessageDefinition() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		when(definition.getName()).thenReturn("template");
		
		VariableResolutionError error = new VariableResolutionError("var", definition);
		
		assertThat(error.getMessage(), containsString("accessed in <template>"));
	}
	
	@Test
	public void testGetMessageScopeSimple() throws Exception {
		Scope scope = mock(Scope.class, RETURNS_DEEP_STUBS);
		when(scope.getDefinition().getName()).thenReturn("child");
		when(scope.getVariables()).thenReturn(asList(var("a", string("a"))));
		when(scope.getParent().getDefinition().getName()).thenReturn("parent");
		when(scope.getParent().getVariables()).thenReturn(asList(var("b", string("b")), var("c", string("c"))));
		when(scope.getParent().getParent()).thenReturn(null);
		
		VariableResolutionError error = new VariableResolutionError("var", scope);
		String message = error.getMessage();
		
		assertThat(message, containsString("scope stack:"));
		assertThat(message, containsString("child:[a='a']"));
		assertThat(message, containsString("parent:[b='b', c='c']"));
	}
	
}
