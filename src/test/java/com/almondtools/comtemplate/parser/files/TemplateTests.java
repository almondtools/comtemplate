package com.almondtools.comtemplate.parser.files;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.ANTLRFileStream;

import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.parser.ComtemplateLexer;
import com.almondtools.comtemplate.parser.ComtemplateParser;
import com.almondtools.comtemplate.parser.MultiChannelTokenStream;
import com.almondtools.comtemplate.parser.TemplateErrorListener;
import com.almondtools.comtemplate.parser.TemplateGroupBuilder;

public class TemplateTests {

	public static TemplateGroup compileLibrary(String fileName) throws IOException {
		return TemplateGroupBuilder.library(fileName, fileName).buildGroup();
	}

	public static TemplateDefinition compileMain(String fileName) throws IOException {
		return TemplateGroupBuilder.main(fileName, fileName).buildMain();
	}

	public static TemplateDefinition compileMainFromText(String text) throws IOException {
		return TemplateGroupBuilder.main("", new ByteArrayInputStream(text.getBytes())).buildMain();
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
