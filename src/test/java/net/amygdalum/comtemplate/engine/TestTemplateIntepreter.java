package net.amygdalum.comtemplate.engine;

import static net.amygdalum.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static net.amygdalum.comtemplate.engine.ResolverRegistry.defaultRegistry;

import org.mockito.Mockito;

public class TestTemplateIntepreter extends SilentTemplateInterpreter {

	public TestTemplateIntepreter(TemplateLoader loader, ResolverRegistry resolvers, GlobalTemplates templates, ErrorHandler handler) {
		super(loader, resolvers, templates, handler);
	}

	public static TestTemplateIntepreter interpreter() {
		return new TestTemplateIntepreter(Mockito.mock(TemplateLoader.class), defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

}
