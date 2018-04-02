package net.amygdalum.comtemplate.parser.files;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;

import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateGroup;
import net.amygdalum.comtemplate.parser.ComtemplateLexer;
import net.amygdalum.comtemplate.parser.ComtemplateParser;
import net.amygdalum.comtemplate.parser.MultiChannelTokenStream;
import net.amygdalum.comtemplate.parser.TemplateErrorListener;
import net.amygdalum.comtemplate.parser.TemplateGroupBuilder;

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
