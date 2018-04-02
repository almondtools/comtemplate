package net.amygdalum.comtemplate.engine.resolvers;

import java.util.ArrayList;
import java.util.List;

import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.Evaluated;
import net.amygdalum.comtemplate.engine.expressions.RawText;

public final class Normalizations {
	
	private Normalizations() {
	}

	public static List<TemplateImmediateExpression> compact(List<TemplateImmediateExpression> expressions) {
		List<TemplateImmediateExpression> compact = new ArrayList<>();
		RawText lastText = null;
		for (TemplateImmediateExpression expression : expressions) {
			if (expression instanceof RawText) {
				if (lastText == null) {
					lastText = (RawText) expression;
				} else {
					lastText = new RawText(lastText.getText() + expression.getText());
				}
			} else {
				if (lastText != null) {
					compact.add(lastText);
					lastText = null;
				}
				if (expression instanceof Evaluated) {
					compact.add(new Evaluated(compact(((Evaluated) expression).getEvaluated())));
				} else {
					compact.add(expression);
				}
			}
		}
		if (lastText != null) {
			compact.add(lastText);
			lastText = null;
		}
		return compact;
	}

}
