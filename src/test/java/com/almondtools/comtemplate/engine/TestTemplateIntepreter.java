package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;

import org.mockito.Mockito;

public class TestTemplateIntepreter extends DefaultTemplateInterpreter {

	public TestTemplateIntepreter(TemplateLoader loader, ResolverRegistry resolvers, GlobalTemplates templates, ErrorHandler handler) {
		super(loader, resolvers, templates, handler);
	}

	public static TestTemplateIntepreter interpreter() {
		return new TestTemplateIntepreter(Mockito.mock(TemplateLoader.class), defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

}
