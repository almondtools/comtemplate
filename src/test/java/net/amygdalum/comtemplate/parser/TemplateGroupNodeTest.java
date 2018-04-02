package net.amygdalum.comtemplate.parser;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.parser.TemplateGroupNode;


public class TemplateGroupNodeTest {

	@Test
	public void testAsMatch() throws Exception {
		assertThat(new TemplateGroupNode("string").as(String.class), equalTo("string"));
	}

	@Test
	public void testAsMismatch() throws Exception {
		assertThat(new TemplateGroupNode("string").as(Integer.class), nullValue());
	}

}
