package com.almondtools.comtemplate.engine;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassPathTemplateLoaderTest {

	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	@Mock
	private ClassLoader classLoader;
	
	private ClassPathTemplateLoader loader;
	
	@Before
	public void before() throws Exception {
		loader = new ClassPathTemplateLoader() {
			@Override
			protected ClassLoader getClassLoader() {
				return classLoader;
			}
		};
	}

	@Test
	public void testAddClassPath() throws Exception {
		ClassPathTemplateLoader loader = new ClassPathTemplateLoader();
		loader.addClassPath("src/test/resources");
		TemplateGroup group = loader.loadGroup("cp.test");
		assertThat(group.getDefinition("test"), instanceOf(ConstantDefinition.class));
	}

	@Test
	public void testAddClassPathPathFails() throws Exception {
		ClassPathTemplateLoader loader = new ClassPathTemplateLoader();
		expected.expect(ClassPathResolutionException.class);
		loader.addClassPath("srctestresources");
	}

	@Test
	public void testAddClassPathLoaderFails() throws Exception {
		expected.expect(ClassPathResolutionException.class);
		loader.addClassPath("src/test/resources");
	}

	@Test
	public void testGetClassLoader() throws Exception {
		assertThat(new ClassPathTemplateLoader().getClassLoader(), equalTo(getClass().getClassLoader()));
	}
	
	@Test
	public void testLoadGroup() throws Exception {
		when(classLoader.getResourceAsStream("mygroup.ctp")).thenReturn(new ByteArrayInputStream("template() ::= {template}".getBytes("UTF-8")));
		
		TemplateGroup group = loader.loadGroup("mygroup");
		
		assertThat(group.getName(), equalTo("mygroup"));
		assertThat(group.getDefinition("template").evaluate(emptyList()), equalTo("template"));
	}
	
	@Test
	public void testLoadGroupIsCached() throws Exception {
		when(classLoader.getResourceAsStream("mygroup.ctp")).thenReturn(new ByteArrayInputStream("template() ::= {template}".getBytes("UTF-8")));

		TemplateGroup group1 = loader.loadGroup("mygroup");
		TemplateGroup group2 = loader.loadGroup("mygroup");
		
		assertThat(group1, sameInstance(group2));
		verify(classLoader, times(1)).getResourceAsStream("mygroup.ctp");
	}
	
	@Test
	public void testLoadGroupFailsWithNull() throws Exception {
		when(classLoader.getResourceAsStream("mygroup.ctp")).thenReturn(null);
		expected.expect(TemplateGroupNotFoundException.class);

		loader.loadGroup("mygroup");
	}
	
	@Test
	public void testLoadGroupFailsWithException() throws Exception {
		when(classLoader.getResourceAsStream("mygroup.ctp")).thenReturn(new InputStream() {
			
			@Override
			public int read() throws IOException {
				throw new IOException();
			}
		});
		expected.expect(TemplateGroupNotFoundException.class);
		
		loader.loadGroup("mygroup");
	}
	
}
