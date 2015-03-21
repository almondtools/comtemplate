package com.almondtools.comtemplate.parser.files;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class ErrorsTest extends TemplateTests {

	@Test
	public void testInvalidCall() throws Exception {
		List<String> errors = findErrors("src/test/resources/errors/invalidcall.ctp");
		
		assertThat(errors, contains("invalid syntax in line 2:1 <call(var=\"x\",secondvar)>"));
	}

	@Test
	public void testIncompleteCall() throws Exception {
		List<String> errors = findErrors("src/test/resources/errors/incompletecall.ctp");
		
		assertThat(errors, contains("invalid syntax in line 2:1 <call(var=\"x\",secondvar>"));
	}

	@Test
	public void testMissingParameterCommaCall() throws Exception {
		List<String> errors = findErrors("src/test/resources/errors/missingparametercomma.ctp");
		
		assertThat(errors, contains("unexpected characters in line 1:18 was <var2>, expected <')', ',', ':', '='>"));
		                            
	}

}
