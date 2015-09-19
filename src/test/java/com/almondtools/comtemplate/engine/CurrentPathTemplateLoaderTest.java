package com.almondtools.comtemplate.engine;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CurrentPathTemplateLoaderTest {

	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	@Before
	public void before() throws Exception {
		Files.write(Paths.get("existing.ctp"), "template ::= {tmp}".getBytes());
	}
	
	@After
	public void after() throws Exception {
		Files.deleteIfExists(Paths.get("existing.ctp"));
	}
	
	@Test
	public void testLoadGroup() throws Exception {
		CurrentPathTemplateLoader loader = new CurrentPathTemplateLoader();
		
		TemplateGroup group = loader.loadGroup("existing");
		
		assertThat(group.getName(), equalTo("existing"));
		assertThat(group.getDefinition("template").evaluate(emptyList()), equalTo("tmp"));
	}
	
	@Test
	public void testLoadGroupFailsWithException() throws Exception {
		CurrentPathTemplateLoader loader = new CurrentPathTemplateLoader();

		expected.expect(TemplateGroupNotFoundException.class);

		loader.loadGroup("notexisting");
	}

	@Test
	public void testClassPathTemplateLoaderTemplateCompiler() throws Exception {
		TemplateCompiler compiler = mock(TemplateCompiler.class);
		CurrentPathTemplateLoader loader = new CurrentPathTemplateLoader(compiler);
		loader.compile("unit", null);
		
		verify(compiler).compile("unit", null, loader);
	}
	

}
