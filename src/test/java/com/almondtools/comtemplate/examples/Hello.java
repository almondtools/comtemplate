package com.almondtools.comtemplate.examples;

import static com.almondtools.comtemplate.engine.TestTemplateIntepreter.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.parser.TemplateGroupBuilder;

public class Hello {
	
	public static void main(String[] args) throws IOException {
		String resource = "src/test/resources/examples/hello.ctp";
		InputStream stream = Files.newInputStream(Paths.get(resource));
		TemplateGroup group = TemplateGroupBuilder.library("hello", resource, stream).buildGroup();
		TemplateDefinition definition = group.getDefinition("hello");
		System.out.println(definition.evaluateNative(interpreter(), "World")); //prints Hello World
	}
}
