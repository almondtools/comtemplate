package com.almondtools.comtemplate.parser.files;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;

import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.parser.ComtemplateLexer;
import com.almondtools.comtemplate.parser.ComtemplateParser;
import com.almondtools.comtemplate.parser.MultiChannelTokenStream;
import com.almondtools.comtemplate.parser.TemplateErrorListener;
import com.almondtools.comtemplate.parser.TemplateGroupBuilder;

public class TemplateTests {

	public static TemplateGroup compileLibrary(String fileName) throws IOException {
		InputStream fileStream = Files.newInputStream(Paths.get(fileName));
		return TemplateGroupBuilder.library(fileName, fileName, fileStream).buildGroup();
	}

	public static TemplateDefinition compileMain(String fileName) throws IOException {
		InputStream fileStream = Files.newInputStream(Paths.get(fileName));
		return TemplateGroupBuilder.main(fileName, fileName, fileStream).buildMain();
	}

	public static TemplateDefinition compileMainFromText(String text) throws IOException {
		return TemplateGroupBuilder.main("test", "test", new ByteArrayInputStream(text.getBytes())).buildMain();
	}

	public static List<String> findErrors(String fileName) throws IOException {
		ComtemplateLexer lexer = new ComtemplateLexer(CharStreams.fromFileName(fileName));
		ComtemplateParser parser = new ComtemplateParser(new MultiChannelTokenStream(lexer));
		TemplateErrorListener errors = new TemplateErrorListener();
		parser.addErrorListener(errors);
		parser.templateFile();
		return errors.getMessages();
	}

}
