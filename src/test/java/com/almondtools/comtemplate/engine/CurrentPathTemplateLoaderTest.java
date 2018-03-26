package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CurrentPathTemplateLoaderTest {

	@BeforeEach
	public void before() throws Exception {
		Files.write(Paths.get("existing.ctp"), "template ::= {tmp}".getBytes());
	}

	@AfterEach
	public void after() throws Exception {
		Files.deleteIfExists(Paths.get("existing.ctp"));
	}

	@Test
	public void testLoadGroup() throws Exception {
		CurrentPathTemplateLoader loader = new CurrentPathTemplateLoader();

		TemplateGroup group = loader.loadGroup("existing");

		assertThat(group.getName(), equalTo("existing"));
		assertThat(group.getDefinition("template").evaluate(interpreter(), emptyList()), equalTo("tmp"));
	}

	@Test
	public void testLoadGroupFailsWithException() throws Exception {
		CurrentPathTemplateLoader loader = new CurrentPathTemplateLoader();

		assertThrows(TemplateGroupNotFoundException.class, () -> loader.loadGroup("notexisting"));
	}

	@Test
	public void testClassPathTemplateLoaderTemplateCompiler() throws Exception {
		TemplateCompiler compiler = mock(TemplateCompiler.class);
		CurrentPathTemplateLoader loader = new CurrentPathTemplateLoader(compiler);
		loader.compile("unit", "unit", null);

		verify(compiler).compileLibrary("unit", "unit", null, loader);
	}

}
