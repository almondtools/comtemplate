package com.almondtools.comtemplate.parser.files;

import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.BailErrorStrategy;

import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.parser.ComtemplateLexer;
import com.almondtools.comtemplate.parser.ComtemplateParser;
import com.almondtools.comtemplate.parser.ComtemplateParser.TemplateFileContext;
import com.almondtools.comtemplate.parser.MultiChannelTokenStream;
import com.almondtools.comtemplate.parser.TemplateErrorListener;
import com.almondtools.comtemplate.parser.TemplateGroupBuilder;

public class TemplateTests {

	public static TemplateGroup compileFile(String fileName) throws IOException {
		ComtemplateLexer lexer = new ComtemplateLexer(new ANTLRFileStream(fileName));
		ComtemplateParser parser = new ComtemplateParser(new MultiChannelTokenStream(lexer));
		parser.setErrorHandler(new BailErrorStrategy());
		TemplateFileContext templateFile = parser.templateFile();
		return new TemplateGroupBuilder(fileName, templateFile).build();
	}

	public static List<String> findErrors(String fileName) throws IOException {
		ComtemplateLexer lexer = new ComtemplateLexer(new ANTLRFileStream(fileName));
		ComtemplateParser parser = new ComtemplateParser(new MultiChannelTokenStream(lexer));
		TemplateErrorListener errors = new TemplateErrorListener();
		parser.addErrorListener(errors);
		parser.templateFile();
		return errors.getMessages();
	}
	
}
