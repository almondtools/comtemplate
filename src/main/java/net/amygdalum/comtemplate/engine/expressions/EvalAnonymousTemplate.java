package net.amygdalum.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;

public class EvalAnonymousTemplate implements TemplateExpression {

	private TemplateDefinition definition;
	private List<TemplateExpression> expressions;

	public EvalAnonymousTemplate(TemplateDefinition definition, List<TemplateExpression> expressions) {
		this.definition = definition;
		this.expressions = stripEnclosingNewLines(expressions);
	}
	
	public EvalAnonymousTemplate(TemplateDefinition definition, TemplateExpression... expressions) {
		this(definition, asList(expressions));
	}
	
	public TemplateDefinition getDefinition() {
		return definition;
	}
	
	public List<TemplateExpression> getExpressions() {
		return expressions;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitEvalAnonymousTemplate(this, scope);
	}

	public static List<TemplateExpression> stripEnclosingNewLines(List<TemplateExpression> expressions) {
		if (!expressions.isEmpty()) {
			List<TemplateExpression> strippedExpressions = new ArrayList<TemplateExpression>(expressions);
			RawText first = firstExpression(strippedExpressions, RawText.class);
			if (first != null) {
				first.setText(first.getText().replaceFirst("^[ \\t]*?(\\r?\\n|\\r)", ""));
			}
			RawText last = lastExpression(expressions, RawText.class);
			if (last != null) {
				last.setText(last.getText().replaceFirst("(\\r?\\n|\\r)[ \\t]*$", ""));
			}
			return strippedExpressions;
		} else {
			return expressions;
		}
	}
	
	public static <T> T firstExpression(List<TemplateExpression> expressions, Class<T> clazz) {
		if (expressions.isEmpty()) {
			return null;
		} else {
			TemplateExpression firstExpression = expressions.get(0);
			if (clazz.isInstance(firstExpression)) {
				return clazz.cast(firstExpression);
			} else {
				return null;
			}
		}
	}

	public static <T> T lastExpression(List<TemplateExpression> expressions, Class<T> clazz) {
		if (expressions.isEmpty()) {
			return null;
		} else {
			TemplateExpression lastExpression = expressions.get(expressions.size() - 1);
			if (clazz.isInstance(lastExpression)) {
				return clazz.cast(lastExpression);
			} else {
				return null;
			}
		}
	}
	
	@Override
	public String toString() {
		return expressions.stream()
			.map(expression -> expression.toString())
			.collect(joining(",", "{", "}"));
	}

}
