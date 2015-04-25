package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.parser.files.TemplateTests.compileFile;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;

public class TestTest {

	private TemplateGroup group;
	private TemplateInterpreter interpreter;

	@Before
	public void before() throws Exception {
		group = compileFile("src/test/resources/test.ctp");
		interpreter = new TemplateInterpreter();
	}

	@Test
	public void testTestTrue() throws Exception {
		ResolvedMapLiteral results = (ResolvedMapLiteral) group.resolveVariable("test")
			.getValue()
			.apply(interpreter, group.groupScope());
		assertThat(results.getAttribute("test1").as(Boolean.class), is(true));
		assertThat(results.getAttribute("test2").as(Boolean.class), is(true));
		assertThat(results.getAttribute("test3").as(Boolean.class), is(true));
	}

	@Test
	public void testTestFalse() throws Exception {
		ResolvedMapLiteral results = (ResolvedMapLiteral) group.resolveVariable("fail")
			.getValue()
			.apply(interpreter, group.groupScope());
		assertThat(results.getAttribute("test1").as(Boolean.class), is(false));
		assertThat(results.getAttribute("test2").as(Boolean.class), is(false));
		assertThat(results.getAttribute("test3").as(Boolean.class), is(false));
	}

}
