package net.amygdalum.comtemplate.parser.files;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ErrorsTest extends TemplateTests {

	@Test
	public void testInvalidCall() throws Exception {
		List<String> errors = findErrors("src/test/resources/errors/invalidcall.ctp");
		
		assertThat(errors, contains("error at 2:2 unexpected token <call(var=\"x\",secondvar)>"));
	}

	@Test
	public void testIncompleteCall() throws Exception {
		List<String> errors = findErrors("src/test/resources/errors/incompletecall.ctp");
		
		assertThat(errors, contains("error at 2:2 unexpected token <call(var=\"x\",secondvar>"));
	}

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
}
