package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.expressions.VariableResolutionError;


public class VariableResolutionErrorTest {

	@Test
	public void testGetMessageBase() throws Exception {
		VariableResolutionError error = new VariableResolutionError("var", (TemplateDefinition) null);
		
		assertThat(error.getMessage(), equalTo("variable <var> cannot be resolved"));
	}

	@Test
	public void testGetMessageDefinition() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		when(definition.getLocation()).thenReturn("resource:template");
		
		VariableResolutionError error = new VariableResolutionError("var", definition);
		
		assertThat(error.getMessage(), containsString("accessed in <resource:template>"));
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
