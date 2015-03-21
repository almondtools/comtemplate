package com.almondtools.comtemplate.engine;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;

import com.almondtools.comtemplate.parser.ComtemplateLexer;
import com.almondtools.comtemplate.parser.ComtemplateParser;
import com.almondtools.comtemplate.parser.TemplateErrorListener;
import com.almondtools.comtemplate.parser.ComtemplateParser.TemplateFileContext;
import com.almondtools.comtemplate.parser.MultiChannelTokenStream;
import com.almondtools.comtemplate.parser.TemplateGroupBuilder;

public abstract class AbstractTemplateLoader implements TemplateLoader {

	public TemplateGroup compile(String name, InputStream stream) throws IOException {
		if (stream == null) {
			throw new TemplateGroupNotFoundException(name);
		}
		ComtemplateLexer lexer = new ComtemplateLexer(new ANTLRInputStream(stream));
		ComtemplateParser parser = new ComtemplateParser(new MultiChannelTokenStream(lexer));
		parser.removeErrorListeners();
		parser.addErrorListener(new TemplateErrorListener());
		TemplateFileContext templateFile = parser.templateFile();
		return new TemplateGroupBuilder(name, templateFile, this).build();
	}

	@Override
	public abstract TemplateGroup loadGroup(String name);
	
	@Override
	public TemplateDefinition loadDefinition(String name) {
		int separator = name.lastIndexOf('.');
		String file = name.substring(0, separator);
		String definition = name.substring(separator + 1);
		TemplateGroup group = loadGroup(file);
		return group.getDefinition(definition);
	}

}
