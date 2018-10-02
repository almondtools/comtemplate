package net.amygdalum.comtemplate.parser.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ErrorsTest extends TemplateTests {

	@Test
	public void testMissingParameterCommaCall() throws Exception {
		List<String> errors = findErrors("src/test/resources/errors/missingparametercomma.ctp");
		
		assertThat(errors, contains("error at 1:18 unexpected token <var2>, expected <')'>"));
	}
	
	@Test
	public void testInvalidDefinitionOp() throws Exception {
		List<String> errors = findErrors("src/test/resources/errors/invaliddefinitionop.ctp");
		
		assertThat(errors, contains(
			"error at 1:23 unexpected token <:>, expected <'::='>",
			"error at 7:23 unexpected token <:>, expected <'::='>",
			"error at 13:23 unexpected token <=>, expected <'::='>",
			"error at 19:23 unexpected token <:>, expected <'::='>"
			));
		
	}

	@Test
	public void testInvalidSyntaxInBody() throws Exception {
		List<String> errors = findErrors("src/test/resources/errors/invalidsyntaxinbody.ctp");
		
		assertThat(errors, hasItems(
			"error at 15:18 unexpected token <>>, expected <'>>'>",
			"error at 28:26 unexpected token <>>, expected <'.', '~', '!', '?', '?:', '>>'>",
			"error at 47:18 unexpected token <>>, expected <'>>'>"
			));
		assertThat(errors, hasSize(3));
	}
	
}
