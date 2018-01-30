package com.almondtools.comtemplate.parser.files;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import com.almondtools.comtemplate.parser.ComtemplateLexer;
import com.almondtools.comtemplate.parser.ComtemplateParser;

public class SmokeTest {

	@Test
	public void testSmoke() throws Exception {
		ComtemplateLexer lexer = new ComtemplateLexer(CharStreams.fromFileName("src/test/resources/smoke.ctp"));
		ComtemplateParser parser = new ComtemplateParser(new CommonTokenStream(lexer));
		parser.setErrorHandler(new BailErrorStrategy());
		parser.templateFile();
	}
}
