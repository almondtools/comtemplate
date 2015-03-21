package com.almondtools.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateExpression;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;

public class EvalAnonymousTemplate implements TemplateExpression {

	private TemplateDefinition definition;
	private List<TemplateExpression> expressions;

	public EvalAnonymousTemplate(TemplateDefinition definition, List<TemplateExpression> expressions) {
		this.definition = definition;
		this.expressions = expressions;
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

	public <T> T firstExpression(Class<T> clazz) {
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

	public <T> T lastExpression(Class<T> clazz) {
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
	
	public void stripEnclosingNewLines() {
		if (!expressions.isEmpty()) {
			RawText first = firstExpression(RawText.class);
			if (first != null) {
				first.setText(first.getText().replaceFirst("^[ \\t]*?(\\r?\\n|\\r)", ""));
			}
			RawText last = lastExpression(RawText.class);
			if (last != null) {
				last.setText(last.getText().replaceFirst("(\\r?\\n|\\r)[ \\t]*$", ""));
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
