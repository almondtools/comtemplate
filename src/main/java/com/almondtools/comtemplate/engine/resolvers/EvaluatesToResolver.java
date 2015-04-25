package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.bool;

import java.util.List;
import java.util.regex.Pattern;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;

public class EvaluatesToResolver extends FunctionResolver {

	private static Pattern SKIP = Pattern.compile("(^\\s+)|(\\s+$)|([\\r\\n]+\\s*)"); 
	private static Pattern COMPRESS = Pattern.compile("\\s+"); 
	
	public EvaluatesToResolver() {
		super("evaluatesTo", 1);
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		String actual = normalized(base.getText());
		String expected = normalized(arguments.get(0).getText());
		return bool(actual.equals(expected));
	}

	private String normalized(String string) {
		String skipped = SKIP.matcher(string).replaceAll("");
		String compressed = COMPRESS.matcher(skipped).replaceAll(" ");
		return compressed;
	}

}
