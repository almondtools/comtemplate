package net.amygdalum.comtemplate.engine;

import java.io.IOException;
import java.io.InputStream;

public interface TemplateCompiler {
	
	TemplateGroup compileLibrary(String name, String resource, InputStream stream, TemplateLoader loader) throws IOException;
	TemplateDefinition compileMain(String name, String resource, InputStream stream, TemplateLoader loader) throws IOException;
	
}
