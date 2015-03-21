package com.almondtools.comtemplate.engine;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractTemplateLoaderTest {

	@Mock
	private TemplateGroup group;

	private AbstractTemplateLoader loader;

	@Before
	public void before() throws Exception {
		loader = new AbstractTemplateLoader() {

			@Override
			public TemplateGroup loadGroup(String name) {
				return group;
			}
		};
	}

	@Test
	public void testLoadDefinition() throws Exception {
		when(group.getDefinition("template")).thenReturn(new TestTemplateDefinition("template"));

		TemplateDefinition definition = loader.loadDefinition("mygroup.template");

		assertThat(definition.evaluate(emptyList()), equalTo("test: "));
	}

}
