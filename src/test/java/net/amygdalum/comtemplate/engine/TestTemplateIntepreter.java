package net.amygdalum.comtemplate.engine;

import static net.amygdalum.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static net.amygdalum.comtemplate.engine.ResolverRegistry.defaultRegistry;

import org.mockito.Mockito;

import net.amygdalum.comtemplate.engine.DefaultErrorHandler;
import net.amygdalum.comtemplate.engine.DefaultTemplateInterpreter;
import net.amygdalum.comtemplate.engine.ErrorHandler;
import net.amygdalum.comtemplate.engine.GlobalTemplates;
import net.amygdalum.comtemplate.engine.ResolverRegistry;
import net.amygdalum.comtemplate.engine.TemplateLoader;

public class TestTemplateIntepreter extends DefaultTemplateInterpreter {

	public TestTemplateIntepreter(TemplateLoader loader, ResolverRegistry resolvers, GlobalTemplates templates, ErrorHandler handler) {
		super(loader, resolvers, templates, handler);
	}

	public static TestTemplateIntepreter interpreter() {
		return new TestTemplateIntepreter(Mockito.mock(TemplateLoader.class), defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

}
