package com.almondtools.comtemplate.parser;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


public class TemplateGroupBuilderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private TemplateGroupBuilder builder;

	@Before
	public void before() throws Exception {
		File newFile = folder.newFile("name.ctg");
		builder = new TemplateGroupBuilder("name", newFile.getAbsolutePath());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testVisitImports() throws Exception {
		builder.visitImports(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitQualifiedName() throws Exception {
		builder.visitQualifiedName(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitQualifiedWildcard() throws Exception {
		builder.visitQualifiedWildcard(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitClauses() throws Exception {
		builder.visitClauses(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitTemplateBody() throws Exception {
		builder.visitTemplateBody(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitParameters() throws Exception {
		builder.visitParameters(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitFunction() throws Exception {
		builder.visitFunction(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitAttributes() throws Exception {
		builder.visitAttributes(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitItems() throws Exception {
		builder.visitItems(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitInlineToken() throws Exception {
		builder.visitInlineToken(null);
	}

}
