package net.amygdalum.comtemplate.parser.files;

import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static net.amygdalum.comtemplate.parser.files.TemplateTests.compileLibrary;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.TemplateGroup;

public class ObjectsTest {

	private TemplateGroup group;

	@BeforeEach
	public void before() throws Exception {
		group = compileLibrary("src/test/resources/objects.ctp");
	}

	@Test
	public void testObjectsAndFields() throws Exception {
		String rendered = group.getDefinition("objectAttributes").evaluate(interpreter());
		assertThat(rendered, equalTo("argument.name = myname\nargument.type = mytype"));
	}

	@Test
	public void testObjectInheritance() throws Exception {
		String rendered = group.getDefinition("objectInheritance").evaluate(interpreter());
		assertThat(rendered, equalTo("[_type=subObject, _supertypes=[definedObject], name=myname, type=mytype, description=mydesc]"));
	}

	@Test
	public void testObjectTransitiveInheritance() throws Exception {
		String rendered = group.getDefinition("objectTransitiveInheritance").evaluate(interpreter());
		assertThat(rendered, equalTo("[_type=subSubObject, _supertypes=[subObject, definedObject], name=myname, type=mytype, description=mydesc]"));
	}

	@Test
	public void testObjectMultipleInheritance() throws Exception {
		String rendered = group.getDefinition("objectMultipleInheritance").evaluate(interpreter());
		assertThat(rendered, equalTo("[_type=sub2Object, _supertypes=[definedObject, otherObject], name=myname, type=mytype, description=mydesc]"));
	}

}
