package com.almondtools.comtemplate.engine;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

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

public class TemplateInterpreterDecorator implements TemplateInterpreter {

	private TemplateInterpreter interpreter;
	private List<InterpreterListener> listeners;
	
	public TemplateInterpreterDecorator(TemplateInterpreter interpreter, InterpreterListener... listeners) {
		this.interpreter = interpreter;
		this.listeners = new ArrayList<InterpreterListener>(asList(listeners));
	}
	
	public void addListener(InterpreterListener listener) {
		listeners.add(listener);
	}

	private TemplateImmediateExpression notifyAndReturn(TemplateExpression source, TemplateImmediateExpression result) {
		for (InterpreterListener listener : listeners) {
			listener.notify(source, result);
		}
		return result;
	}

	@Override
	public TemplateImmediateExpression visitRawText(RawText rawText, Scope scope) {
		return notifyAndReturn(rawText, interpreter.visitRawText(rawText, scope));
	}

	@Override
	public TemplateImmediateExpression visitEvalVar(EvalVar evalVar, Scope scope) {
		return notifyAndReturn(evalVar, interpreter.visitEvalVar(evalVar, scope));
	}

	@Override
	public TemplateImmediateExpression visitEvalContextVar(EvalContextVar evalContextVar, Scope scope) {
		return notifyAndReturn(evalContextVar, interpreter.visitEvalContextVar(evalContextVar, scope));
	}

	@Override
	public TemplateImmediateExpression visitEvalTemplate(EvalTemplate evalTemplate, Scope scope) {
		return notifyAndReturn(evalTemplate, interpreter.visitEvalTemplate(evalTemplate, scope));
	}

	@Override
	public TemplateImmediateExpression visitEvalTemplateFunction(EvalTemplateFunction evalTemplateFunction, Scope scope) {
		return notifyAndReturn(evalTemplateFunction, interpreter.visitEvalTemplateFunction(evalTemplateFunction, scope));
	}

	@Override
	public TemplateImmediateExpression visitEvalAnonymousTemplate(EvalAnonymousTemplate evalAnonymousTemplate, Scope scope) {
		return notifyAndReturn(evalAnonymousTemplate, interpreter.visitEvalAnonymousTemplate(evalAnonymousTemplate, scope));
	}

	@Override
	public TemplateImmediateExpression visitEvalAttribute(EvalAttribute evalAttribute, Scope scope) {
		return notifyAndReturn(evalAttribute, interpreter.visitEvalAttribute(evalAttribute, scope));
	}

	@Override
	public TemplateImmediateExpression visitEvalFunction(EvalFunction evalFunction, Scope scope) {
		return notifyAndReturn(evalFunction, interpreter.visitEvalFunction(evalFunction, scope));
	}

	@Override
	public TemplateImmediateExpression visitEvaluated(Evaluated evaluated, Scope scope) {
		return notifyAndReturn(evaluated, interpreter.visitEvaluated(evaluated, scope));
	}

	@Override
	public TemplateImmediateExpression visitExists(Exists exists, Scope scope) {
		return notifyAndReturn(exists, interpreter.visitExists(exists, scope));
	}

	@Override
	public TemplateImmediateExpression visitDefaulted(Defaulted defaulted, Scope scope) {
		return notifyAndReturn(defaulted, interpreter.visitDefaulted(defaulted, scope));
	}

	@Override
	public TemplateImmediateExpression visitConcat(Concat concat, Scope scope) {
		return notifyAndReturn(concat, interpreter.visitConcat(concat, scope));
	}

	@Override
	public TemplateImmediateExpression visitToObject(ToObject toObject, Scope scope) {
		return notifyAndReturn(toObject, interpreter.visitToObject(toObject, scope));
	}

	@Override
	public TemplateImmediateExpression visitMapLiteral(MapLiteral mapLiteral, Scope scope) {
		return notifyAndReturn(mapLiteral, interpreter.visitMapLiteral(mapLiteral, scope));
	}

	@Override
	public TemplateImmediateExpression visitMapLiteral(ResolvedMapLiteral mapLiteral, Scope scope) {
		return notifyAndReturn(mapLiteral, interpreter.visitMapLiteral(mapLiteral, scope));
	}

	@Override
	public TemplateImmediateExpression visitListLiteral(ListLiteral listLiteral, Scope scope) {
		return notifyAndReturn(listLiteral, interpreter.visitListLiteral(listLiteral, scope));
	}

	@Override
	public TemplateImmediateExpression visitListLiteral(ResolvedListLiteral listLiteral, Scope scope) {
		return notifyAndReturn(listLiteral, interpreter.visitListLiteral(listLiteral, scope));
	}

	@Override
	public TemplateImmediateExpression visitStringLiteral(StringLiteral stringLiteral, Scope scope) {
		return notifyAndReturn(stringLiteral, interpreter.visitStringLiteral(stringLiteral, scope));
	}

	@Override
	public TemplateImmediateExpression visitIntegerLiteral(IntegerLiteral integerLiteral, Scope scope) {
		return notifyAndReturn(integerLiteral, interpreter.visitIntegerLiteral(integerLiteral, scope));
	}

	@Override
	public TemplateImmediateExpression visitDecimalLiteral(DecimalLiteral decimalLiteral, Scope scope) {
		return notifyAndReturn(decimalLiteral, interpreter.visitDecimalLiteral(decimalLiteral, scope));
	}

	@Override
	public TemplateImmediateExpression visitBooleanLiteral(BooleanLiteral booleanLiteral, Scope scope) {
		return notifyAndReturn(booleanLiteral, interpreter.visitBooleanLiteral(booleanLiteral, scope));
	}

	@Override
	public TemplateImmediateExpression visitNativeObject(NativeObject nativeObject, Scope scope) {
		return notifyAndReturn(nativeObject, interpreter.visitNativeObject(nativeObject, scope));
	}

	@Override
	public TemplateImmediateExpression visitCast(Cast cast, Scope scope) {
		return notifyAndReturn(cast, interpreter.visitCast(cast, scope));
	}

	@Override
	public TemplateImmediateExpression visitErrorExpression(ErrorExpression errorExpression, Scope scope) {
		return notifyAndReturn(errorExpression, interpreter.visitErrorExpression(errorExpression, scope));
	}
	
}
