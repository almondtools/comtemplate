package net.amygdalum.comtemplate.parser.files;

import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static net.amygdalum.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.amygdalum.comtemplate.engine.DefaultErrorHandler;
import net.amygdalum.comtemplate.engine.DefaultTemplateInterpreter;
import net.amygdalum.comtemplate.engine.GlobalTemplates;
import net.amygdalum.comtemplate.engine.ResolverRegistry;
import net.amygdalum.comtemplate.engine.TemplateGroup;
import net.amygdalum.comtemplate.engine.TemplateInterpreter;
import net.amygdalum.comtemplate.engine.TemplateLoader;
import net.amygdalum.comtemplate.engine.expressions.ResolvedMapLiteral;

public class GlobalDataTest extends TemplateTests {

	private TemplateLoader loader;
	private TemplateGroup group;

	@BeforeEach
	public void before() throws Exception {
		loader = Mockito.mock(TemplateLoader.class);
		group = compileLibrary("src/test/resources/globaldata.ctp");
	}

	@Test
	public void testGlobalData() throws Exception {
		ResolverRegistry resolvers = defaultRegistry();
		GlobalTemplates globals = defaultTemplates();
		globals.register(var("global", new ResolvedMapLiteral(var("mystring", string("my string")))));
		DefaultErrorHandler errors = new DefaultErrorHandler();
		TemplateInterpreter interpreter = new DefaultTemplateInterpreter(loader, resolvers, globals, errors);
		
		String rendered = group.getDefinition("globalDataRule").evaluate(interpreter, null, emptyList()).getText();
		
		assertThat(rendered, equalTo("this data is global: my string"));
	}

}
