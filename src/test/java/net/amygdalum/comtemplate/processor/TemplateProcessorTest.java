package net.amygdalum.comtemplate.processor;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import net.amygdalum.comtemplate.engine.ComtemplateException;
import net.amygdalum.comtemplate.engine.Messages;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateInterpreter;
import net.amygdalum.util.extension.TemporaryFolder;
import net.amygdalum.util.extension.TemporaryFolderExtension;

@ExtendWith(TemporaryFolderExtension.class)
public class TemplateProcessorTest {

	@Test
	public void testTemplateProcessorRunSingleFile(TemporaryFolder folder) throws Exception {
		Path targetpath = folder.resolve("processor");
		TemplateProcessor processor = new TemplateProcessor("src/test/resources/processor/single", targetpath.toString(), createProperties());

		processor.run();

		assertTrue(Files.exists(targetpath.resolve("source.html")));
	}

	@Test
	public void testTemplateProcessorRunSingleNestedFile(TemporaryFolder folder) throws Exception {
		Path targetpath = folder.resolve("processor");
		TemplateProcessor processor = new TemplateProcessor("src/test/resources/processor/nested", targetpath.toString(), createProperties());

		processor.run();

		assertTrue(Files.exists(targetpath.resolve("nested/source.html")));
	}

	@Test
	public void testTemplateProcessorCreatedDirectFileContainsCorrectStrings(TemporaryFolder folder) throws Exception {
		Path targetpath = folder.resolve("processor");
		TemplateProcessor processor = new TemplateProcessor("src/test/resources/processor/single", targetpath.toString(), createProperties());

		processor.run();

		assertThat(new String(Files.readAllBytes(targetpath.resolve("source.html"))), containsString("Hello World"));
	}

	@Test
	public void testTemplateProcessorCreatedNestedFileContainsCorrectStrings(TemporaryFolder folder) throws Exception {
		Path targetpath = folder.resolve("processor");
		TemplateProcessor processor = new TemplateProcessor("src/test/resources/processor/nested", targetpath.toString(), createProperties());

		processor.run();

		assertThat(new String(Files.readAllBytes(targetpath.resolve("nested/source.html"))), containsString("Hello Nested World"));
	}

	public Properties createProperties() {
		Properties properties = new Properties();
		properties.setProperty("classpath", "true");
		properties.setProperty("libraries", ",src/test/resources/processorlib");
		return properties;
	}

	@Test
	public void testTemplateProcessorRunUnqualifiedFile(TemporaryFolder folder) throws Exception {
		Path targetpath = folder.resolve("processor");
		TemplateProcessor processor = new TemplateProcessor("src/test/resources/processor/unqualified", targetpath.toString(), createProperties());

		processor.run();

		assertFalse(Files.exists(targetpath.resolve("source.html")));
	}

	@Test
	public void testTemplateProcessorRunProjectsFiles(TemporaryFolder folder) throws Exception {
		Path targetpath = folder.resolve("processor");
		TemplateProcessor processor = new TemplateProcessor("src/test/resources/processor/projection", targetpath.toString(), createProperties());

		processor.run();

		assertTrue(Files.exists(targetpath.resolve("source1.html")));
		assertTrue(Files.exists(targetpath.resolve("source2.html")));
		assertTrue(Files.exists(targetpath.resolve("source.html")));
	}

	@Test
	public void testTemplateProcessorRunProjectsFilesWithValidity(TemporaryFolder folder) throws Exception {
		Path targetpath = folder.resolve("processor");
		TemplateProcessor processor = new TemplateProcessor("src/test/resources/processor/projectionvalid", targetpath.toString(), createProperties());

		processor.run();

		assertTrue(Files.exists(targetpath.resolve("source1.html")));
		assertTrue(Files.exists(targetpath.resolve("source2.html")));
		assertFalse(Files.exists(targetpath.resolve("source.html")));
	}

	@Test
	public void testValidPath(TemporaryFolder folder) throws Exception {
		Path existing = folder.provideFolder("existing");

		assertThat(TemplateProcessor.validPath(existing.toString()), equalTo(existing));

		Path notExisting = folder.resolve("notExisting");
		assertThrows(FileNotFoundException.class, () -> TemplateProcessor.validPath(notExisting.toString()));
	}

	@Test
	public void testValidTargetPath(TemporaryFolder folder) throws Exception {
		Path existing = folder.provideFolder("existing");

		assertThat(TemplateProcessor.validTargetPath(existing.toString()), equalTo(existing));

		Path notExisting = folder.resolve("notExisting");
		assertThat(TemplateProcessor.validTargetPath(notExisting.toString()), equalTo(notExisting));

		Path file = folder.provideFile("file", new byte[0]);
		assertThrows(FileNotFoundException.class, () -> TemplateProcessor.validTargetPath(file.toString()));
	}

	@Test
	public void testMainWithProperties(TemporaryFolder folder) throws Exception {
		Path target = folder.resolve("target");

		TemplateProcessor.main("src/test/resources/processor/exampleproperties", target.toString());

		assertTrue(Files.exists(target.resolve("source.chtml")));
	}

	@Test
	public void testMainWithNoProperties(TemporaryFolder folder) throws Exception {
		Path target = folder.resolve("target");

		TemplateProcessor.main("src/test/resources/processor/examplenoproperties", target.toString());

		assertTrue(Files.exists(target.resolve("source.html")));
	}

	@Test
	public void testMainWithMissingArguments() throws Exception {
		Messages errors = Mockito.mock(Messages.class);
		Messages.setERROR(errors);

		TemplateProcessor.main();

		verify(errors).log("signature: java net.amygdalum.comtemplate.processor.TemplateProcessor <source path> <target path>");
	}

	@Test
	public void testMainWithBrokenArguments(TemporaryFolder folder) throws Exception {
		Messages errors = Mockito.mock(Messages.class);
		Messages.setERROR(errors);

		TemplateProcessor.main(folder.resolve("notexisting").toString(), folder.resolve("alsonotexisting").toString());

		verify(errors).log("signature: java net.amygdalum.comtemplate.processor.TemplateProcessor <source path> <target path>");
	}

	@Test
	public void testRunWithError(TemporaryFolder folder) throws Exception {
		Messages errors = Mockito.mock(Messages.class);
		Messages.setERROR(errors);
		Path targetpath = folder.resolve("processor");
		TemplateProcessor processor = new TemplateProcessor("src/test/resources/processor/single", targetpath.toString(), createProperties()) {
			@Override
			protected void generateMain(String templateFileName, TemplateDefinition main, TemplateInterpreter interpreter) {
				throw new ComtemplateException() {
					@Override
					public String getMessage() {
						return "error!";
					}
				};
			}
		};

		processor.run();

		ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
		verify(errors).log(message.capture());
		assertThat(message.getValue(), containsString("failed template generation:"));
		assertThat(message.getValue(), containsString("error!"));
	}

	@Test
	public void testFindAllSources(TemporaryFolder folder) throws Exception {
		Path sourcepath = folder.provideFolder("src");
		Path targetpath = folder.resolve("target");

		Files.createFile(sourcepath.resolve("file.ctp"));
		Files.createFile(sourcepath.resolve("file.txt"));

		TemplateProcessor processor = new TemplateProcessor(sourcepath.toString(), targetpath.toString(), createProperties());

		assertThat(processor.findAllSources(), contains("file.ctp"));
	}

}
