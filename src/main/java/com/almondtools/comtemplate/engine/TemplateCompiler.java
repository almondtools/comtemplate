package com.almondtools.comtemplate.engine;

import java.io.IOException;
import java.io.InputStream;

public interface TemplateCompiler {
	
	TemplateGroup compile(String name, InputStream stream, TemplateLoader loader) throws IOException;
	
}
