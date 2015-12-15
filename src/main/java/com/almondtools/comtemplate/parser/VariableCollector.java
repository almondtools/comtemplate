package com.almondtools.comtemplate.parser;

import java.util.LinkedHashSet;
import java.util.Set;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpression;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;
import com.almondtools.comtemplate.engine.expressions.BooleanLiteral;
import com.almondtools.comtemplate.engine.expressions.Cast;
import com.almondtools.comtemplate.engine.expressions.Concat;
import com.almondtools.comtemplate.engine.expressions.DecimalLiteral;
import com.almondtools.comtemplate.engine.expressions.Defaulted;
import com.almondtools.comtemplate.engine.expressions.ErrorExpression;
import com.almondtools.comtemplate.engine.expressions.EvalAnonymousTemplate;
import com.almondtools.comtemplate.engine.expressions.EvalAttribute;
import com.almondtools.comtemplate.engine.expressions.EvalContextVar;
import com.almondtools.comtemplate.engine.expressions.EvalFunction;
import com.almondtools.comtemplate.engine.expressions.EvalTemplate;
import com.almondtools.comtemplate.engine.expressions.EvalTemplateFunction;
import com.almondtools.comtemplate.engine.expressions.EvalVar;
import com.almondtools.comtemplate.engine.expressions.EvalVirtual;
import com.almondtools.comtemplate.engine.expressions.Evaluated;
import com.almondtools.comtemplate.engine.expressions.Exists;
import com.almondtools.comtemplate.engine.expressions.IntegerLiteral;
import com.almondtools.comtemplate.engine.expressions.ListLiteral;
import com.almondtools.comtemplate.engine.expressions.MapLiteral;
import com.almondtools.comtemplate.engine.expressions.NativeObject;
import com.almondtools.comtemplate.engine.expressions.RawText;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;
import com.almondtools.comtemplate.engine.expressions.StringLiteral;
import com.almondtools.comtemplate.engine.expressions.ToObject;

public class VariableCollector implements TemplateExpressionVisitor<TemplateExpression> {

	private Set<String> variables;

	public VariableCollector() {
		this.variables = new LinkedHashSet<String>();
	}
	
	public Set<String> getVariables() {
		return variables;
	}

	@Override
	public TemplateExpression visitRawText(RawText rawText, Scope scope) {
		return rawText;
	}

	@Override
	public TemplateExpression visitEvalVar(EvalVar evalVar, Scope scope) {
		variables.add(evalVar.getName());
		return evalVar;
	}

	@Override
	public TemplateExpression visitEvalContextVar(EvalContextVar evalContextVar, Scope scope) {
		return evalContextVar;
	}

	@Override
	public TemplateExpression visitEvalTemplate(EvalTemplate evalTemplate, Scope scope) {
		evalTemplate.getArguments().stream().map(variable -> variable.getValue())
			.forEach(expression -> expression.apply(this, scope));
		return evalTemplate;
	}

	@Override
	public TemplateExpression visitEvalTemplateFunction(EvalTemplateFunction evalTemplateFunction, Scope scope) {
		evalTemplateFunction.getArguments().stream()
			.forEach(expression -> expression.apply(this, scope));
		return evalTemplateFunction;
	}

	@Override
	public TemplateExpression visitEvalAnonymousTemplate(EvalAnonymousTemplate evalAnonymousTemplate, Scope scope) {
		evalAnonymousTemplate.getExpressions().stream()
			.forEach(expression -> expression.apply(this, scope));
		return evalAnonymousTemplate;
	}

	@Override
	public TemplateExpression visitEvalAttribute(EvalAttribute evalAttribute, Scope scope) {
		evalAttribute.getBase().apply(this, scope);
		return evalAttribute;
	}

	@Override
	public TemplateExpression visitEvalVirtual(EvalVirtual evalAttribute, Scope scope) {
		evalAttribute.getBase().apply(this, scope);
		evalAttribute.getAttribute().apply(this, scope);
		return evalAttribute;
	}

	@Override
	public TemplateExpression visitEvalFunction(EvalFunction evalFunction, Scope scope) {
		evalFunction.getArguments().stream()
			.forEach(expression -> expression.apply(this, scope));
		return evalFunction;
	}

	@Override
	public TemplateExpression visitEvaluated(Evaluated evaluated, Scope scope) {
		return evaluated;
	}

	@Override
	public TemplateExpression visitExists(Exists exists, Scope scope) {
		exists.getExpression().apply(this, scope);
		return exists;
	}

	@Override
	public TemplateExpression visitDefaulted(Defaulted defaulted, Scope scope) {
		defaulted.getExpression().apply(this, scope);
		defaulted.getDefaultExpression().apply(this, scope);
		return defaulted;
	}

	@Override
	public TemplateExpression visitConcat(Concat concat, Scope scope) {
		concat.getExpressions().stream()
			.forEach(expression -> expression.apply(this, scope));
		return concat;
	}

	@Override
	public TemplateExpression visitToObject(ToObject toObject, Scope scope) {
		toObject.getExpression().apply(this, scope);
		return toObject;
	}

	@Override
	public TemplateExpression visitMapLiteral(MapLiteral mapLiteral, Scope scope) {
		mapLiteral.getMap().values().stream()
			.forEach(expression -> expression.apply(this, scope));
		return mapLiteral;
	}

	@Override
	public TemplateExpression visitMapLiteral(ResolvedMapLiteral mapLiteral, Scope scope) {
		return mapLiteral;
	}

	@Override
	public TemplateExpression visitListLiteral(ListLiteral listLiteral, Scope scope) {
		listLiteral.getList().stream()
			.forEach(expression -> expression.apply(this, scope));
		return listLiteral;
	}

	@Override
	public TemplateExpression visitListLiteral(ResolvedListLiteral listLiteral, Scope scope) {
		return listLiteral;
	}

	@Override
	public TemplateExpression visitStringLiteral(StringLiteral stringLiteral, Scope scope) {
		return stringLiteral;
	}

	@Override
	public TemplateExpression visitIntegerLiteral(IntegerLiteral integerLiteral, Scope scope) {
		return integerLiteral;
	}

	@Override
	public TemplateExpression visitDecimalLiteral(DecimalLiteral decimalLiteral, Scope scope) {
		return decimalLiteral;
	}

	@Override
	public TemplateExpression visitBooleanLiteral(BooleanLiteral booleanLiteral, Scope scope) {
		return booleanLiteral;
	}

	@Override
	public TemplateExpression visitNativeObject(NativeObject nativeObject, Scope scope) {
		return nativeObject;
	}

	@Override
	public TemplateExpression visitCast(Cast cast, Scope scope) {
		cast.getExpression().apply(this, scope);
		return cast;
	}

	@Override
	public TemplateExpression visitErrorExpression(ErrorExpression errorExpression, Scope scope) {
		return errorExpression;
	}

}
