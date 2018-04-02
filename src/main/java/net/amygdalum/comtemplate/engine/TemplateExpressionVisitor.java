package net.amygdalum.comtemplate.engine;

import net.amygdalum.comtemplate.engine.expressions.BooleanLiteral;
import net.amygdalum.comtemplate.engine.expressions.Cast;
import net.amygdalum.comtemplate.engine.expressions.Concat;
import net.amygdalum.comtemplate.engine.expressions.DecimalLiteral;
import net.amygdalum.comtemplate.engine.expressions.Defaulted;
import net.amygdalum.comtemplate.engine.expressions.ErrorExpression;
import net.amygdalum.comtemplate.engine.expressions.EvalAnonymousTemplate;
import net.amygdalum.comtemplate.engine.expressions.EvalAttribute;
import net.amygdalum.comtemplate.engine.expressions.EvalContextVar;
import net.amygdalum.comtemplate.engine.expressions.EvalFunction;
import net.amygdalum.comtemplate.engine.expressions.EvalTemplate;
import net.amygdalum.comtemplate.engine.expressions.EvalTemplateFunction;
import net.amygdalum.comtemplate.engine.expressions.EvalTemplateMixed;
import net.amygdalum.comtemplate.engine.expressions.EvalVar;
import net.amygdalum.comtemplate.engine.expressions.EvalVirtual;
import net.amygdalum.comtemplate.engine.expressions.Evaluated;
import net.amygdalum.comtemplate.engine.expressions.Exists;
import net.amygdalum.comtemplate.engine.expressions.IgnoreErrors;
import net.amygdalum.comtemplate.engine.expressions.IntegerLiteral;
import net.amygdalum.comtemplate.engine.expressions.ListLiteral;
import net.amygdalum.comtemplate.engine.expressions.MapLiteral;
import net.amygdalum.comtemplate.engine.expressions.NativeObject;
import net.amygdalum.comtemplate.engine.expressions.RawText;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;
import net.amygdalum.comtemplate.engine.expressions.ResolvedMapLiteral;
import net.amygdalum.comtemplate.engine.expressions.StringLiteral;
import net.amygdalum.comtemplate.engine.expressions.ToObject;

public interface TemplateExpressionVisitor<T> {

	T visitRawText(RawText rawText, Scope scope);

	T visitEvalVar(EvalVar evalVar, Scope scope);

	T visitEvalContextVar(EvalContextVar evalContextVar, Scope scope);

	T visitEvalTemplate(EvalTemplate evalTemplate, Scope scope);

	T visitEvalTemplateFunction(EvalTemplateFunction evalTemplateFunction, Scope scope);

	T visitEvalTemplateMixed(EvalTemplateMixed evalTemplateMixed, Scope scope);

	T visitEvalAnonymousTemplate(EvalAnonymousTemplate evalAnonymousTemplate, Scope scope);

	T visitEvalAttribute(EvalAttribute evalAttribute, Scope scope);

	T visitEvalVirtual(EvalVirtual evalAttribute, Scope scope);

	T visitEvalFunction(EvalFunction evalFunction, Scope scope);

	T visitEvaluated(Evaluated evaluated, Scope scope);

	T visitIgnoreErrors(IgnoreErrors ignoreErrors, Scope scope);

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
