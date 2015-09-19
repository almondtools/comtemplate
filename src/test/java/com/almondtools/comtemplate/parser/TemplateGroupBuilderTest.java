package com.almondtools.comtemplate.parser;

import static com.almondtools.util.exceptions.ExceptionMatcher.matchesException;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.almondtools.comtemplate.engine.TemplateGroupException;


public class TemplateGroupBuilderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private TemplateGroupBuilder builder;

	@Before
	public void before() throws Exception {
		File newFile = folder.newFile("name.ctp");
		builder = new TemplateGroupBuilder("name", newFile.getAbsolutePath());
	}
	
	@Test
	public void testTemplateGroupBuilderStringInputStream() throws Exception {
		InputStream stream = new ByteArrayInputStream("template ::= {}".getBytes());

		builder = new TemplateGroupBuilder("name", stream);
		
		assertThat(builder.build().getDefinitions(), hasSize(1));
	}

	@Test
	public void testTemplateGroupBuilderStringInputStreamWithErrors() throws Exception {
		thrown.expect(matchesException(TemplateGroupException.class)
			.withMessage(containsString("template group 'name'")));
		
		InputStream stream = new ByteArrayInputStream("broken template ::= {}".getBytes());
		builder = new TemplateGroupBuilder("name", stream);
	}

	@Test
	public void testTemplateGroupBuilderStringString() throws Exception {
		Path file = Paths.get(folder.getRoot().getPath()).resolve("name.ctp");
		Files.write(file, "template ::= {}".getBytes());
		
		builder = new TemplateGroupBuilder("name", file.toString());
		
		assertThat(builder.build().getDefinitions(), hasSize(1));
	}

	@Test
	public void testTemplateGroupBuilderStringStringWithErrors() throws Exception {
		thrown.expect(matchesException(TemplateGroupException.class)
			.withMessage(both(containsString("name.ctp")).and(containsString("template group 'name'"))));
		
		Path file = Paths.get(folder.getRoot().getPath()).resolve("name.ctp");
		Files.write(file, "broken template ::= {}".getBytes());
		builder = new TemplateGroupBuilder("name", file.toString());
		
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

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitSingleQuoteToken() throws Exception {
		builder.visitSingleQuoteToken(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitDoubleQuoteToken() throws Exception {
		builder.visitDoubleQuoteToken(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testVisitRefTemplateError() throws Exception {
		builder.visitRefTemplateError(null);
	}
}
