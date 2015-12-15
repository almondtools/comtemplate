package com.almondtools.comtemplate.engine;

import java.io.IOException;
import java.io.InputStream;

public interface TemplateCompiler {
	
	TemplateGroup compileLibrary(String name, InputStream stream, TemplateLoader loader) throws IOException;
	TemplateDefinition compileMain(String name, InputStream stream, TemplateLoader loader) throws IOException;
	
}
