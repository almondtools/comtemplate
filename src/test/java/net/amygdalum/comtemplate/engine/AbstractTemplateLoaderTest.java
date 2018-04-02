package net.amygdalum.comtemplate.engine;

import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.AbstractTemplateLoader;
import net.amygdalum.comtemplate.engine.TemplateDefinition;

public class AbstractTemplateLoaderTest {

	private AbstractTemplateLoader loader;

	@BeforeEach
	public void before() throws Exception {
		loader = new AbstractTemplateLoader() {

			@Override
			public InputStream loadSource(String name) {
				return new ByteArrayInputStream("template() ::= {mytest}".getBytes());
			}
			@Override
			public String resolveResource(String name) {
				return "test";
			}

		};
	}

	@Test
	public void testLoadDefinition() throws Exception {
		TemplateDefinition definition = loader.loadDefinition("mygroup.template");

		assertThat(definition.evaluate(interpreter(), emptyList()), equalTo("mytest"));
	}

}
