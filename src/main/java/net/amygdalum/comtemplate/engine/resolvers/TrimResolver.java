package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static net.amygdalum.comtemplate.engine.resolvers.Normalizations.compact;
import static net.amygdalum.util.stream.ByIndex.byIndex;

import java.util.List;
import java.util.function.Function;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.Evaluated;
import net.amygdalum.comtemplate.engine.expressions.NativeObject;
import net.amygdalum.comtemplate.engine.expressions.RawText;
import net.amygdalum.comtemplate.engine.expressions.StringLiteral;

public class TrimResolver extends FunctionResolver {

	public TrimResolver() {
		super("trim");
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (base instanceof StringLiteral) {
			return string(((StringLiteral) base).getText().trim());
		} else if (base instanceof RawText) {
			return new RawText(((RawText) base).getText().trim());
		} else if (base instanceof Evaluated) {
			List<TemplateImmediateExpression> evaluated = compact(((Evaluated) base).getEvaluated());
			if (evaluated.isEmpty()) {
				return base;
			} else if (evaluated.size() == 1) {
				return new Evaluated(resolve(evaluated.get(0), arguments, scope));
			} else {
				return new Evaluated(evaluated.stream()
					.map(byIndex(evaluated.size()))
					.map(item -> {
						if (item.first) {
							return trim(item.value, s -> s.replaceAll("^\\s+", ""));
						} else if (item.last) {
							return trim(item.value, s -> s.replaceAll("\\s+$", ""));
						} else {
							return item.value;
						}
					})
					.collect(toList()));
			}
		} else if (base instanceof NativeObject) {
			Object object = ((NativeObject) base).getObject();
			if (object instanceof String) {
				return string(((String) object).trim());
			} else {
				return base;
			}
		} else {
			return base;
		}
	}
	
	protected TemplateImmediateExpression trim(TemplateImmediateExpression base, Function<String, String> trim) {
		if (base instanceof StringLiteral) {
			return string(trim.apply(((StringLiteral) base).getText()));
		} else if (base instanceof RawText) {
			return new RawText(trim.apply(((RawText) base).getText()));
		} else if (base instanceof NativeObject) {
			Object object = ((NativeObject) base).getObject();
			if (object instanceof String) {
				return new NativeObject(trim.apply(((String) object)));
			} else {
				return base;
			}
		} else {
			return base;
		}
	}

	@Override
	public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
		return asList(TemplateImmediateExpression.class);
	}

}
