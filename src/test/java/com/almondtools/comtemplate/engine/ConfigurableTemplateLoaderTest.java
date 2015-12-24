package com.almondtools.comtemplate.engine;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurableTemplateLoaderTest {

	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Mock
	private ClassLoader classLoader;

	@Test
	public void testForPaths() throws Exception {
		ConfigurableTemplateLoader loader = new ConfigurableTemplateLoader()
			.forPaths("src/test/resources");
		TemplateGroup group = loader.loadGroup("cp.test");
		assertThat(group.getDefinition("test"), instanceOf(ValueDefinition.class));
	}

	@Test
	public void testAddClassPathPathFails() throws Exception {
		ConfigurableTemplateLoader loader = new ConfigurableTemplateLoader()
			.forPaths("srctestresources");
		expected.expect(TemplateGroupNotFoundException.class);

		loader.loadGroup("cp.test");
	}

	public ConfigurableTemplateLoader newConfigurableTemplateLoaderWithMockedClassLoader() {
		return new ConfigurableTemplateLoader() {
			@Override
			protected ClassLoader getClassLoader() {
				return classLoader;
			}
		}.withClasspath(true);
	}

	@Test
	public void testGetClassLoader() throws Exception {
		assertThat(new ConfigurableTemplateLoader().getClassLoader(), equalTo(getClass().getClassLoader()));
	}

	@Test
	public void testLoadGroup() throws Exception {
		ConfigurableTemplateLoader loader = newConfigurableTemplateLoaderWithMockedClassLoader();
		when(classLoader.getResourceAsStream("mygroup.ctp")).thenReturn(new ByteArrayInputStream("template() ::= {template}".getBytes("UTF-8")));

		TemplateGroup group = loader.loadGroup("mygroup");

		assertThat(group.getName(), equalTo("mygroup"));
		assertThat(group.getDefinition("template").evaluate(emptyList()), equalTo("template"));
	}

	@Test
	public void testLoadGroupIsCached() throws Exception {
		ConfigurableTemplateLoader loader = newConfigurableTemplateLoaderWithMockedClassLoader();
		when(classLoader.getResourceAsStream("mygroup.ctp")).thenReturn(new ByteArrayInputStream("template() ::= {template}".getBytes("UTF-8")));

		TemplateGroup group1 = loader.loadGroup("mygroup");
		TemplateGroup group2 = loader.loadGroup("mygroup");

		assertThat(group1, sameInstance(group2));
		verify(classLoader, times(1)).getResourceAsStream("mygroup.ctp");
	}

	@Test
	public void testLoadGroupFailsWithNull() throws Exception {
		ConfigurableTemplateLoader loader = newConfigurableTemplateLoaderWithMockedClassLoader();
		when(classLoader.getResourceAsStream("mygroup.ctp")).thenReturn(null);
		expected.expect(TemplateGroupNotFoundException.class);

		loader.loadGroup("mygroup");
	}

	@Test
	public void testLoadGroupFailsWithException() throws Exception {
		ConfigurableTemplateLoader loader = newConfigurableTemplateLoaderWithMockedClassLoader();
		when(classLoader.getResourceAsStream("mygroup.ctp")).thenReturn(new InputStream() {

			@Override
			public int read() throws IOException {
				throw new IOException();
			}
		});
		expected.expect(TemplateGroupNotFoundException.class);

		loader.loadGroup("mygroup");
	}

	@Test
	public void testConfigurableTemplateLoaderTemplateCompiler() throws Exception {
		TemplateCompiler compiler = mock(TemplateCompiler.class);
		ConfigurableTemplateLoader loader = new ConfigurableTemplateLoader(compiler);
		loader.compile("unit", null);

		verify(compiler).compileLibrary("unit", null, loader);
	}

}