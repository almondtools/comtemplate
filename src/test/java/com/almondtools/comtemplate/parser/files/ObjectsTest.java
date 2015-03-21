package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.parser.files.TemplateTests.compileFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.TemplateGroup;

public class ObjectsTest {

	private TemplateGroup group;

	@Before
	public void before() throws Exception {
		group = compileFile("src/test/resources/objects.ctp");
	}

	@Test
	public void testObjectsAndFields() throws Exception {
		String rendered = group.getDefinition("objectAttributes").evaluate();
		assertThat(rendered, equalTo("argument.name = myname\nargument.type = mytype"));
	}

	@Test
	public void testObjectInheritance() throws Exception {
		String rendered = group.getDefinition("objectInheritance").evaluate();
		assertThat(rendered, equalTo("[_type=subObject, _supertypes=[definedObject], name=myname, type=mytype, description=mydesc]"));
	}

	@Test
	public void testObjectTransitiveInheritance() throws Exception {
		String rendered = group.getDefinition("objectTransitiveInheritance").evaluate();
		assertThat(rendered, equalTo("[_type=subSubObject, _supertypes=[subObject, definedObject], name=myname, type=mytype, description=mydesc]"));
	}

	@Test
	public void testObjectMultipleInheritance() throws Exception {
		String rendered = group.getDefinition("objectMultipleInheritance").evaluate();
		assertThat(rendered, equalTo("[_type=sub2Object, _supertypes=[definedObject, otherObject], name=myname, type=mytype, description=mydesc]"));
	}

}
