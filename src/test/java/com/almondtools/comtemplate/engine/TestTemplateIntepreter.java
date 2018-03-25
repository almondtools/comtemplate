package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;

public class TestTemplateIntepreter extends DefaultTemplateInterpreter {

	public TestTemplateIntepreter(ResolverRegistry resolvers, GlobalTemplates templates, ErrorHandler handler) {
		super(resolvers, templates, handler);
	}

	public static TestTemplateIntepreter interpreter() {
		return new TestTemplateIntepreter(defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
	}

}
