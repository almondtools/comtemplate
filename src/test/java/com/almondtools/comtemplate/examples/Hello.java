package com.almondtools.comtemplate.examples;

import java.io.IOException;

import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.parser.TemplateGroupBuilder;

public class Hello {
	
	public static void main(String[] args) throws IOException {
		TemplateGroup group = TemplateGroupBuilder.library("hello", "src/test/resources/examples/hello.ctp").buildGroup();
		TemplateDefinition definition = group.getDefinition("hello");
		System.out.println(definition.evaluateNative("World")); //prints Hello World
	}
}
