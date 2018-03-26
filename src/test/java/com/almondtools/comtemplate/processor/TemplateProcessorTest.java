package com.almondtools.comtemplate.processor;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.jupiter.api.Test;


public class TemplateProcessorTest {

	@Test
	public void testTemplateProcessorRunCreatesFiles() throws Exception {
		TemplateProcessor processor = new TemplateProcessor("src/test/resources/processor", "target/temp/processor", createProperties());
		
		processor.run();
		
		Path targetpath = Paths.get("target/temp/processor");
		assertTrue(Files.exists(targetpath.resolve("source.html")));
		assertTrue(Files.exists(targetpath.resolve("nested/source.html")));
	}

	@Test
	public void testTemplateProcessorCreatedDirectFileContainsCorrectStrings() throws Exception {
		TemplateProcessor processor = new TemplateProcessor("src/test/resources/processor", "target/temp/processor", createProperties());
		
		processor.run();
		
		Path targetpath = Paths.get("target/temp/processor");
		assertThat(new String(Files.readAllBytes(targetpath.resolve("source.html"))), containsString("Hello World"));
	}

	@Test
	public void testTemplateProcessorCreatedNestedFileContainsCorrectStrings() throws Exception {
		TemplateProcessor processor = new TemplateProcessor("src/test/resources/processor", "target/temp/processor", createProperties());
		
		processor.run();
		
		Path targetpath = Paths.get("target/temp/processor");
		assertThat(new String(Files.readAllBytes(targetpath.resolve("nested/source.html"))), containsString("Hello Nested World"));
	}

	public Properties createProperties() {
		Properties properties = new Properties();
		properties.setProperty("classpath", "true");
		properties.setProperty("libraries", "src/test/resources/processorlib");
		return properties;
	}

}
