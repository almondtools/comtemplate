package net.amygdalum.comtemplate.engine.resolvers;

import static java.lang.Character.isWhitespace;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static net.amygdalum.util.stream.Windowed.windowed;

import java.util.List;
import java.util.stream.Stream;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.Evaluated;
import net.amygdalum.comtemplate.engine.expressions.NativeObject;
import net.amygdalum.comtemplate.engine.expressions.RawText;
import net.amygdalum.comtemplate.engine.expressions.StringLiteral;


public class CompressResolver extends FunctionResolver {

	public CompressResolver() {
		super("compress");
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (base instanceof StringLiteral) {
			return string(((StringLiteral) base).getText().trim().replaceAll("\\s+", " "));
		} else if (base instanceof RawText) {
			return new RawText(((RawText) base).getText().trim().replaceAll("\\s+", " "));
		} else if (base instanceof Evaluated) {
			List<TemplateImmediateExpression> evaluated = Normalizations.compact(((Evaluated) base).getEvaluated());
			if (evaluated.isEmpty()) {
				return base;
			} else if (evaluated.size() == 1) {
				return new Evaluated(resolve(evaluated.get(0), arguments, scope));
			} else {
				return new Evaluated(windowed(evaluated.stream(), new TemplateImmediateExpression[2])
					.flatMap(window -> {
						if (window[0] == null) {
							return Stream.of(resolve(window[1], arguments, scope));
						} else if (endsWithWhitespace(window[0].getText()) || startsWithWhitespace(window[1].getText())) {
							return Stream.of(new RawText(" "), resolve(window[1], arguments, scope));
						} else {
							return Stream.of(resolve(window[1], arguments, scope));
						}
					})
					.collect(toList()));
			}
		} else if (base instanceof NativeObject) {
			Object object = ((NativeObject) base).getObject();
			if (object instanceof String) {
				return new NativeObject(((String) object).trim().replaceAll("\\s+", " "));
			} else {
				return base;
			}
		} else {
			return base;
		}
	}

	private boolean startsWithWhitespace(String text) {
		if (text == null || text.isEmpty()) {
			return false;
		}
		return isWhitespace(text.charAt(0));
	}

	private boolean endsWithWhitespace(String text) {
		if (text == null || text.isEmpty()) {
			return false;
		}
		return isWhitespace(text.charAt(text.length() - 1));
	}
	
	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(TemplateImmediateExpression.class);
	}

}
