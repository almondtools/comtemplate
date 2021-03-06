package net.amygdalum.comtemplate.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.parser.ComtemplateParser.FunctionDefinitionContext;

public class TemplateBodyContextTest {

	@Test
	public void testOuterWhitespacesAreIgnored() throws Exception {
		FunctionDefinitionContext ctx = parseBodyContext("tmp() ::= {  <html id=\"1\"> `content` </html> } ");
		assertThat(ctx.getText(), equalTo("tmp()::={  <html id=\"1\"> `content` </html> }"));
	}

	private FunctionDefinitionContext parseBodyContext(String text) {
		ComtemplateLexer lexer = new ComtemplateLexer(CharStreams.fromString(text));
		ComtemplateParser parser = new ComtemplateParser(new MultiChannelTokenStream(lexer));
		parser.setErrorHandler(new BailErrorStrategy());
		return parser.functionDefinition();
	}
}
