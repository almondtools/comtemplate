package com.almondtools.comtemplate.engine;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractTemplateLoaderTest {

	private AbstractTemplateLoader loader;

	@Before
	public void before() throws Exception {
		loader = new AbstractTemplateLoader() {

			@Override
			public InputStream loadSource(String name) {
				return new ByteArrayInputStream("template() ::= {mytest}".getBytes());
			}

		};
	}

	@Test
	public void testLoadDefinition() throws Exception {
		TemplateDefinition definition = loader.loadDefinition("mygroup.template");

		assertThat(definition.evaluate(emptyList()), equalTo("mytest"));
	}

}
