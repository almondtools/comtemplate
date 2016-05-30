package com.almondtools.comtemplate.parser;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.junit.Test;

import com.almondtools.comtemplate.parser.ComtemplateParser.TemplateBodyContext;

public class TemplateDefinitionContextTest {

	@Test
	public void testInnerWhitespacesAreKept() throws Exception {
		TemplateBodyContext ctx = parseBodyContext("  <html id=\"1\"> <<content>> </html> ");
		assertThat(ctx.getText(), equalTo("  <html id=\"1\"> <<content>> </html> "));
	}

	@Test
	public void testReferenceWhitespacesAreIgnored() throws Exception {
		TemplateBodyContext ctx = parseBodyContext("  <html id=\"1\"> <<content(a=b, c=d)>> </html> ");
		assertThat(ctx.getText(), equalTo("  <html id=\"1\"> <<content(a=b,c=d)>> </html> "));
	}
	
	@Test
	public void testCommentsAreSkipped() throws Exception {
		TemplateBodyContext ctx = parseBodyContext("  <html id=\"1\">#begin html\n <<content(a=b, c=d)>> #body\n</html>#end html\n ");
		assertThat(ctx.getText(), equalTo("  <html id=\"1\">\n <<content(a=b,c=d)>> \n</html>\n "));
	}
	
	@Test
	public void testEscapedCommentsAreAllowed() throws Exception {
		TemplateBodyContext ctx = parseBodyContext("  <html id=\"1\"> <<content(a=b, c=d)>> \\#escaped</html> ");
		assertThat(ctx.getText(), equalTo("  <html id=\"1\"> <<content(a=b,c=d)>> \\#escaped</html> "));
	}

	private TemplateBodyContext parseBodyContext(String text) {
		ComtemplateLexer lexer = new ComtemplateLexer(new ANTLRInputStream(text));
		ComtemplateParser parser = new ComtemplateParser(new MultiChannelTokenStream(lexer));
		parser.setErrorHandler(new BailErrorStrategy());
		return parser.templateBody();
	}
}
