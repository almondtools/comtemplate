package net.amygdalum.comtemplate.parser;

import static com.almondtools.conmatch.exceptions.ExceptionMatcher.matchesException;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import net.amygdalum.comtemplate.engine.TemplateGroupException;
import net.amygdalum.util.extension.TemporaryFolder;
import net.amygdalum.util.extension.TemporaryFolderExtension;

@ExtendWith(TemporaryFolderExtension.class)
public class TemplateGroupBuilderTest {

	private TemporaryFolder folder;

	@BeforeEach
	public void before(TemporaryFolder folder) throws Exception {
		this.folder = folder;
		Files.createFile(folder.resolve("name.ctp"));
	}

	@Test
	public void testGetName() throws Exception {
		InputStream stream = new ByteArrayInputStream("template ::= {}".getBytes());
		
		assertThat(TemplateGroupBuilder.library("name1", "resource", stream).getName(), equalTo("name1"));
		assertThat(TemplateGroupBuilder.main("name2", "resource", stream).getName(), equalTo("name2"));
	}

	@Test
	public void testTemplateGroupBuilderStringInputStream(TemporaryFolder folder) throws Exception {
		InputStream stream = new ByteArrayInputStream("template ::= {}".getBytes());

		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", "resource", stream);

		assertThat(builder.buildGroup().getDefinitions(), hasSize(1));
	}

	@Test
	public void testTemplateGroupBuilderStringInputStreamWithErrors() throws Exception {
		TemplateGroupException thrown = assertThrows(TemplateGroupException.class, () -> {
			InputStream stream = new ByteArrayInputStream("broken template ::= {}".getBytes());
			TemplateGroupBuilder.library("name", "resource", stream);
		});
		assertThat(thrown, matchesException(TemplateGroupException.class)
			.withMessage(containsString("parsing template group <name> in file 'resource' failed")));
	}

	@Test
	public void testTemplateGroupBuilderStringString(TemporaryFolder folder) throws Exception {
		Path file = folder.resolve("name.ctp");
		Files.write(file, "template ::= {}".getBytes());

		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", file.toString(), Files.newInputStream(file));

		assertThat(builder.buildGroup().getDefinitions(), hasSize(1));
	}

	@Test
	public void testTemplateGroupBuilderStringStringWithErrors(TemporaryFolder folder) throws Exception {
		Path file = folder.resolve("name.ctp");
		TemplateGroupException thrown = assertThrows(TemplateGroupException.class, () -> {
			Files.write(file, "broken template ::= {}".getBytes());
			TemplateGroupBuilder.library("name", file.toString(), Files.newInputStream(file));
		});
		assertThat(thrown, matchesException(TemplateGroupException.class)
			.withMessage(both(containsString("name.ctp")).and(containsString("parsing template group <name> in file '" + file.toString() + "' failed"))));
	}

	@Test
	public void testVisitImports() throws Exception {
		Path newFile = folder.resolve("name.ctp");
		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", newFile.toString(), Files.newInputStream(newFile));
		assertThrows(UnsupportedOperationException.class, () -> builder.visitImports(null));
	}

	@Test
	public void testVisitQualifiedName() throws Exception {
		Path newFile = folder.resolve("name.ctp");
		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", newFile.toString(), Files.newInputStream(newFile));
		assertThrows(UnsupportedOperationException.class, () -> builder.visitQualifiedName(null));
	}

	@Test
	public void testVisitQualifiedWildcard() throws Exception {
		Path newFile = folder.resolve("name.ctp");
		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", newFile.toString(), Files.newInputStream(newFile));
		assertThrows(UnsupportedOperationException.class, () -> builder.visitQualifiedWildcard(null));
	}

	@Test
	public void testVisitClauses() throws Exception {
		Path newFile = folder.resolve("name.ctp");
		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", newFile.toString(), Files.newInputStream(newFile));
		assertThrows(UnsupportedOperationException.class, () -> builder.visitClauses(null));
	}

	@Test
	public void testVisitParameters() throws Exception {
		Path newFile = folder.resolve("name.ctp");
		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", newFile.toString(), Files.newInputStream(newFile));
		assertThrows(UnsupportedOperationException.class, () -> builder.visitParameters(null));
	}

	@Test
	public void testVisitFunction() throws Exception {
		Path newFile = folder.resolve("name.ctp");
		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", newFile.toString(), Files.newInputStream(newFile));
		assertThrows(UnsupportedOperationException.class, () -> builder.visitFunction(null));
	}

	@Test
	public void testVisitAttributes() throws Exception {
		Path newFile = folder.resolve("name.ctp");
		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", newFile.toString(), Files.newInputStream(newFile));
		assertThrows(UnsupportedOperationException.class, () -> builder.visitAttributes(null));
	}

	@Test
	public void testVisitItems() throws Exception {
		Path newFile = folder.resolve("name.ctp");
		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", newFile.toString(), Files.newInputStream(newFile));
		assertThrows(UnsupportedOperationException.class, () -> builder.visitItems(null));
	}

	@Test
	public void testVisitInlineToken() throws Exception {
		Path newFile = folder.resolve("name.ctp");
		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", newFile.toString(), Files.newInputStream(newFile));
		assertThrows(UnsupportedOperationException.class, () -> builder.visitInlineToken(null));
	}

	@Test
	public void testVisitSingleQuoteToken() throws Exception {
		Path newFile = folder.resolve("name.ctp");
		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", newFile.toString(), Files.newInputStream(newFile));
		assertThrows(UnsupportedOperationException.class, () -> builder.visitSingleQuoteToken(null));
	}

	@Test
	public void testVisitDoubleQuoteToken() throws Exception {
		Path newFile = folder.resolve("name.ctp");
		TemplateGroupBuilder builder = TemplateGroupBuilder.library("name", newFile.toString(), Files.newInputStream(newFile));
		assertThrows(UnsupportedOperationException.class, () -> builder.visitDoubleQuoteToken(null));
	}

}
