package net.amygdalum.comtemplate.engine;

import java.util.ArrayList;
import java.util.List;

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

public class DefaultTemplateInterpreter extends SilentTemplateInterpreter {

    private List<InterpreterListener> listeners;

    public DefaultTemplateInterpreter(TemplateLoader loader, ResolverRegistry resolvers, GlobalTemplates templates, ErrorHandler handler) {
        super(loader, resolvers, templates, handler);
        this.listeners = new ArrayList<InterpreterListener>();
    }

    public void addListener(InterpreterListener listener) {
        listeners.add(listener);
    }

    private TemplateImmediateExpression emit(Scope scope, TemplateExpression source, TemplateImmediateExpression result) {
        for (InterpreterListener listener : listeners) {
            listener.notify(scope, source, result);
        }
        return result;
    }

    @Override
    public TemplateImmediateExpression visitRawText(RawText rawText, Scope scope) {
        return emit(scope, rawText, super.visitRawText(rawText, scope));
    }

    @Override
    public TemplateImmediateExpression visitEvalVar(EvalVar evalVar, Scope scope) {
        return emit(scope, evalVar, super.visitEvalVar(evalVar, scope));
    }

    @Override
    public TemplateImmediateExpression visitEvalContextVar(EvalContextVar evalContextVar, Scope scope) {
        return emit(scope, evalContextVar, super.visitEvalContextVar(evalContextVar, scope));
    }

    @Override
    public TemplateImmediateExpression visitEvalTemplate(EvalTemplate evalTemplate, Scope scope) {
        return emit(scope, evalTemplate, super.visitEvalTemplate(evalTemplate, scope));
    }

    @Override
    public TemplateImmediateExpression visitEvalTemplateFunction(EvalTemplateFunction evalTemplateFunction, Scope scope) {
        return emit(scope, evalTemplateFunction, super.visitEvalTemplateFunction(evalTemplateFunction, scope));
    }

    @Override
    public TemplateImmediateExpression visitEvalTemplateMixed(EvalTemplateMixed evalTemplateMixed, Scope scope) {
        return emit(scope, evalTemplateMixed, super.visitEvalTemplateMixed(evalTemplateMixed, scope));
    }

    @Override
    public TemplateImmediateExpression visitEvalAnonymousTemplate(EvalAnonymousTemplate evalAnonymousTemplate, Scope scope) {
        return emit(scope, evalAnonymousTemplate, super.visitEvalAnonymousTemplate(evalAnonymousTemplate, scope));
    }

    @Override
    public TemplateImmediateExpression visitEvalAttribute(EvalAttribute evalAttribute, Scope scope) {
        return emit(scope, evalAttribute, super.visitEvalAttribute(evalAttribute, scope));
    }

    @Override
    public TemplateImmediateExpression visitEvalFunction(EvalFunction evalFunction, Scope scope) {
        return emit(scope, evalFunction, super.visitEvalFunction(evalFunction, scope));
    }

    @Override
    public TemplateImmediateExpression visitEvalVirtual(EvalVirtual evalAttribute, Scope scope) {
        return emit(scope, evalAttribute, super.visitEvalVirtual(evalAttribute, scope));
    }

    @Override
    public TemplateImmediateExpression visitEvaluated(Evaluated evaluated, Scope scope) {
        return emit(scope, evaluated, super.visitEvaluated(evaluated, scope));
    }

    @Override
    public TemplateImmediateExpression visitIgnoreErrors(IgnoreErrors ignoreErrors, Scope scope) {
        return emit(scope, ignoreErrors, super.visitIgnoreErrors(ignoreErrors, scope));
    }

    @Override
    public TemplateImmediateExpression visitExists(Exists exists, Scope scope) {
        return emit(scope, exists, super.visitExists(exists, scope));
    }

    @Override
    public TemplateImmediateExpression visitDefaulted(Defaulted defaulted, Scope scope) {
        return emit(scope, defaulted, super.visitDefaulted(defaulted, scope));
    }

    @Override
    public TemplateImmediateExpression visitConcat(Concat concat, Scope scope) {
        return emit(scope, concat, super.visitConcat(concat, scope));
    }

    @Override
    public TemplateImmediateExpression visitToObject(ToObject toObject, Scope scope) {
        return emit(scope, toObject, super.visitToObject(toObject, scope));
    }

    @Override
    public TemplateImmediateExpression visitMapLiteral(MapLiteral mapLiteral, Scope scope) {
        return emit(scope, mapLiteral, super.visitMapLiteral(mapLiteral, scope));
    }

    @Override
    public TemplateImmediateExpression visitMapLiteral(ResolvedMapLiteral mapLiteral, Scope scope) {
        return emit(scope, mapLiteral, super.visitMapLiteral(mapLiteral, scope));
    }

    @Override
    public TemplateImmediateExpression visitListLiteral(ListLiteral listLiteral, Scope scope) {
        return emit(scope, listLiteral, super.visitListLiteral(listLiteral, scope));
    }

    @Override
    public TemplateImmediateExpression visitListLiteral(ResolvedListLiteral listLiteral, Scope scope) {
        return emit(scope, listLiteral, super.visitListLiteral(listLiteral, scope));
    }

    @Override
    public TemplateImmediateExpression visitStringLiteral(StringLiteral stringLiteral, Scope scope) {
        return emit(scope, stringLiteral, super.visitStringLiteral(stringLiteral, scope));
    }

    @Override
    public TemplateImmediateExpression visitIntegerLiteral(IntegerLiteral integerLiteral, Scope scope) {
        return emit(scope, integerLiteral, super.visitIntegerLiteral(integerLiteral, scope));
    }

    @Override
    public TemplateImmediateExpression visitDecimalLiteral(DecimalLiteral decimalLiteral, Scope scope) {
        return emit(scope, decimalLiteral, super.visitDecimalLiteral(decimalLiteral, scope));
    }

    @Override
    public TemplateImmediateExpression visitBooleanLiteral(BooleanLiteral booleanLiteral, Scope scope) {
        return emit(scope, booleanLiteral, super.visitBooleanLiteral(booleanLiteral, scope));
    }

    @Override
    public TemplateImmediateExpression visitNativeObject(NativeObject nativeObject, Scope scope) {
        return emit(scope, nativeObject, super.visitNativeObject(nativeObject, scope));
    }

    @Override
    public TemplateImmediateExpression visitCast(Cast cast, Scope scope) {
        return emit(scope, cast, super.visitCast(cast, scope));
    }

    @Override
    public TemplateImmediateExpression visitErrorExpression(ErrorExpression errorExpression, Scope scope) {
        return emit(scope, errorExpression, super.visitErrorExpression(errorExpression, scope));
    }

}
