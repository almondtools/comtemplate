package net.amygdalum.comtemplate.examples.html;

import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;

import java.io.IOException;
import java.util.stream.Stream;

import net.amygdalum.comtemplate.engine.ComtemplateException;
import net.amygdalum.comtemplate.engine.CurrentPathTemplateLoader;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateLoader;

public class HtmlGenerator {

	public static void main(String[] args) throws IOException {
		String name = args[0];
		Object[] arguments = Stream.of(args).skip(1).toArray(len -> new Object[len]);

		TemplateLoader loader = new CurrentPathTemplateLoader();
		try {
			TemplateDefinition template = loader.loadDefinition(name);
			
			System.out.println(template.evaluateNative(interpreter(), arguments));
		} catch (ComtemplateException e) {
			System.out.println(e.getMessage());
		}
	}

}
