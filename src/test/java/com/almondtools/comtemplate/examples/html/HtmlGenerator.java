package com.almondtools.comtemplate.examples.html;

import java.io.IOException;
import java.util.stream.Stream;

import com.almondtools.comtemplate.engine.ComtemplateException;
import com.almondtools.comtemplate.engine.CurrentPathTemplateLoader;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateLoader;

public class HtmlGenerator {

	public static void main(String[] args) throws IOException {
		String name = args[0];
		Object[] arguments = Stream.of(args).skip(1).toArray(len -> new Object[len]);

		TemplateLoader loader = new CurrentPathTemplateLoader();
		try {
			TemplateDefinition template = loader.loadDefinition(name);
			
			System.out.println(template.evaluateNative(arguments));
		} catch (ComtemplateException e) {
			System.out.println(e.getMessage());
		}
	}

}
