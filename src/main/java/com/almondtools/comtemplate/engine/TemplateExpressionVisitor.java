package com.almondtools.comtemplate.engine;

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

public interface TemplateExpressionVisitor<T> {

	T visitRawText(RawText rawText, Scope scope);

	T visitEvalVar(EvalVar evalVar, Scope scope);

	T visitEvalContextVar(EvalContextVar evalContextVar, Scope scope);

	T visitEvalTemplate(EvalTemplate evalTemplate, Scope scope);

	T visitEvalTemplateFunction(EvalTemplateFunction evalTemplateFunction, Scope scope);

	T visitEvalAnonymousTemplate(EvalAnonymousTemplate evalAnonymousTemplate, Scope scope);

	T visitEvalAttribute(EvalAttribute evalAttribute, Scope scope);

	T visitEvalVirtual(EvalVirtual evalAttribute, Scope scope);

	T visitEvalFunction(EvalFunction evalFunction, Scope scope);

	T visitEvaluated(Evaluated evaluated, Scope scope);

	T visitExists(Exists exists, Scope scope);

	T visitDefaulted(Defaulted defaulted, Scope scope);

	T visitConcat(Concat concat, Scope scope);

	T visitToObject(ToObject toObject, Scope scope);

	T visitMapLiteral(MapLiteral mapLiteral, Scope scope);

	T visitMapLiteral(ResolvedMapLiteral mapLiteral, Scope scope);

	T visitListLiteral(ListLiteral listLiteral, Scope scope);

	T visitListLiteral(ResolvedListLiteral listLiteral, Scope scope);

	T visitStringLiteral(StringLiteral stringLiteral, Scope scope);

	T visitIntegerLiteral(IntegerLiteral integerLiteral, Scope scope);

	T visitDecimalLiteral(DecimalLiteral decimalLiteral, Scope scope);

	T visitBooleanLiteral(BooleanLiteral booleanLiteral, Scope scope);

	T visitNativeObject(NativeObject nativeObject, Scope scope);

	T visitCast(Cast cast, Scope scope);

	T visitErrorExpression(ErrorExpression errorExpression, Scope scope);

}
