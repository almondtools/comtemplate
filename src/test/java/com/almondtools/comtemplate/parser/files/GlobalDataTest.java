package com.almondtools.comtemplate.parser.files;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.DefaultErrorHandler;
import com.almondtools.comtemplate.engine.DefaultTemplateInterpreter;
import com.almondtools.comtemplate.engine.GlobalTemplates;
import com.almondtools.comtemplate.engine.ResolverRegistry;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateInterpreter;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;

public class GlobalDataTest extends TemplateTests {

	private TemplateGroup group;

	@Before
	public void before() throws Exception {
		group = compileFile("src/test/resources/globaldata.ctp");
	}

	@Test
	public void testGlobalData() throws Exception {
		ResolverRegistry resolvers = defaultRegistry();
		GlobalTemplates globals = defaultTemplates();
		globals.register(var("global", new ResolvedMapLiteral(var("mystring", string("my string")))));
		DefaultErrorHandler errors = new DefaultErrorHandler();
		TemplateInterpreter interpreter = new DefaultTemplateInterpreter(resolvers, globals, errors);
		
		String rendered = group.getDefinition("globalDataRule").evaluate(interpreter, null, emptyList()).getText();
		
		assertThat(rendered, equalTo("this data is global: my string"));
	}

}
