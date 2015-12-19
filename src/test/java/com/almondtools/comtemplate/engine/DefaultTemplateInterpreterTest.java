package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateParameter.param;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.ListLiteral.list;
import static com.almondtools.comtemplate.engine.expressions.MapLiteral.map;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

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
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.IntegerLiteral;
import com.almondtools.comtemplate.engine.expressions.NativeObject;
import com.almondtools.comtemplate.engine.expressions.RawText;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;
import com.almondtools.comtemplate.engine.expressions.StringLiteral;
import com.almondtools.comtemplate.engine.expressions.TemplateResolutionError;
import com.almondtools.comtemplate.engine.expressions.ToObject;
import com.almondtools.comtemplate.engine.expressions.UnexpectedTypeError;
import com.almondtools.comtemplate.engine.expressions.VariableResolutionError;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTemplateInterpreterTest {

	@Rule
	public ExpectedException expected = ExpectedException.none();
	@Mock
	private GlobalTemplates globals;
	@Mock
	private ResolverRegistry resolvers;
	@Mock
	private ErrorHandler handler;
	@Mock
	private Scope scope;

	private DefaultTemplateInterpreter interpreter;

	@Before
	public void before() throws Exception {
		interpreter = new DefaultTemplateInterpreter(resolvers, globals, handler);
	}

	@Test
	public void testVisitRawText() throws Exception {
		RawText rawText = new RawText("raw text");

		TemplateImmediateExpression result = interpreter.visitRawText(rawText, scope);

		assertThat(result, sameInstance(rawText));
	}

	@Test
	public void testVisitEvalVarSuccessful() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		when(scope.resolveVariable("var", definition)).thenReturn(var("var", string("string")));

		TemplateImmediateExpression result = interpreter.visitEvalVar(new EvalVar("var", definition), scope);

		assertThat(result, equalTo(string("string")));
	}

	@Test
	public void testVisitEvalVarFailed() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		when(scope.resolveVariable("var", definition)).thenReturn(null);

		TemplateImmediateExpression result = interpreter.visitEvalVar(new EvalVar("var", definition), scope);

		assertThat(result, instanceOf(VariableResolutionError.class));
	}

	@Test
	public void testVisitEvalContextVarGlobal() throws Exception {
		when(scope.resolveContextVariable("var")).thenReturn(null);
		when(globals.resolveGlobal("var")).thenReturn(var("var", integer(22)));

		TemplateImmediateExpression result = interpreter.visitEvalContextVar(new EvalContextVar("var"), scope);

		assertThat(result, equalTo(integer(22)));
	}

	@Test
	public void testVisitEvalContextVarSuccesful() throws Exception {
		when(scope.resolveContextVariable("var")).thenReturn(var("var", integer(22)));

		TemplateImmediateExpression result = interpreter.visitEvalContextVar(new EvalContextVar("var"), scope);

		assertThat(result, equalTo(integer(22)));
	}

	@Test
	public void testVisitEvalContextVarFailed() throws Exception {
		when(scope.resolveContextVariable("var")).thenReturn(null);

		TemplateImmediateExpression result = interpreter.visitEvalContextVar(new EvalContextVar("var"), scope);

		assertThat(result, instanceOf(VariableResolutionError.class));
	}

	@Test
	public void testVisitEvalTemplateResolvedGlobally() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		when(globals.resolve("template")).thenReturn(new TestTemplateDefinition("template", param("param")));

		TemplateImmediateExpression result = interpreter.visitEvalTemplate(new EvalTemplate("template", definition, var("param", integer(2))), scope);

		assertThat(result, equalTo(string("test: param=2")));
	}

	@Test
	public void testVisitEvalTemplateResolvedInScope() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		when(scope.resolveTemplate("template", definition)).thenReturn(new TestTemplateDefinition("template", param("param")));

		TemplateImmediateExpression result = interpreter.visitEvalTemplate(new EvalTemplate("template", definition, var("param", integer(3))), scope);

		assertThat(result, equalTo(string("test: param=3")));
	}

	@Test
	public void testVisitEvalTemplateNotResolved() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);

		TemplateImmediateExpression result = interpreter.visitEvalTemplate(new EvalTemplate("template", definition, var("param", integer(4))), scope);

		assertThat(result, instanceOf(TemplateResolutionError.class));
	}

	@Test
	public void testVisitEvalTemplateFunctionResolvedGlobally() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		when(globals.resolve("template")).thenReturn(new TestTemplateDefinition("template", param("param")));
		TemplateImmediateExpression result = interpreter.visitEvalTemplateFunction(new EvalTemplateFunction("template", definition, integer(2)), scope);

		assertThat(result, equalTo(string("test: param=2")));
	}

	@Test
	public void testVisitEvalTemplateFunctionResolvedInScope() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		when(scope.resolveTemplate("template", definition)).thenReturn(new TestTemplateDefinition("template", param("param")));
		TemplateImmediateExpression result = interpreter.visitEvalTemplateFunction(new EvalTemplateFunction("template", definition, integer(3)), scope);

		assertThat(result, equalTo(string("test: param=3")));
	}

	@Test
	public void testVisitEvalTemplateFunctionNotResolved() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);

		TemplateImmediateExpression result = interpreter.visitEvalTemplateFunction(new EvalTemplateFunction("template", definition, integer(4)), scope);

		assertThat(result, instanceOf(TemplateResolutionError.class));
	}

	@Test
	public void testVisitEvalAnonymousTemplate() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		TemplateImmediateExpression result = interpreter.visitEvalAnonymousTemplate(new EvalAnonymousTemplate(definition, string("test:"), integer(33)), scope);

		assertThat(result.getText(), equalTo("test:33"));
	}

	@Test
	public void testVisitEvalAttribute() throws Exception {
		TemplateExpression base = map(var("key", string("value")));
		when(resolvers.getResolverFor(any(ResolvedMapLiteral.class))).thenReturn(new TestResolver(ResolvedMapLiteral.class));
		TemplateImmediateExpression result = interpreter.visitEvalAttribute(new EvalAttribute(base, "attribute"), scope);

		assertThat(result, equalTo(string("[key='value'].attribute()")));
	}

	@Test
	public void testVisitEvalVirtual() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class, RETURNS_DEEP_STUBS);
		when(scope.resolveVariable("var", definition)).thenReturn(TemplateVariable.var("var", string("key")));
		TemplateExpression base = map(var("key", string("value")));
		when(resolvers.getResolverFor(any(ResolvedMapLiteral.class))).thenReturn(new TestResolver(ResolvedMapLiteral.class));
		TemplateImmediateExpression result = interpreter.visitEvalVirtual(new EvalVirtual(base, new EvalVar("var", definition)), scope);

		assertThat(result, equalTo(string("[key='value'].key()")));
	}

	@Test
	public void testVisitEvalFunction() throws Exception {
		TemplateExpression base = map(var("key", string("value")));
		when(resolvers.getResolverFor(Mockito.any(ResolvedMapLiteral.class))).thenReturn(new TestResolver(ResolvedMapLiteral.class));
		TemplateImmediateExpression result = interpreter.visitEvalFunction(new EvalFunction(base, "attribute", integer(1), integer(2)), scope);

		assertThat(result, equalTo(string("[key='value'].attribute(1,2)")));
	}

	@Test
	public void testVisitEvaluated() throws Exception {
		Evaluated evaluated = new Evaluated(emptyList());
		TemplateImmediateExpression result = interpreter.visitEvaluated(evaluated, scope);

		assertThat(result, sameInstance(evaluated));
	}

	@Test
	public void testVisitExistsOnConstant() throws Exception {
		TemplateImmediateExpression result = interpreter.visitExists(new Exists(string("exists")), scope);

		assertThat(result, equalTo(TRUE));
	}

	@Test
	public void testVisitExistsOnResolved() throws Exception {
		when(scope.resolveContextVariable("resolvable")).thenReturn(var("resolvable", string("exists")));
		TemplateImmediateExpression result = interpreter.visitExists(new Exists(new EvalContextVar("resolvable")), scope);

		assertThat(result, equalTo(TRUE));
	}

	@Test
	public void testVisitExistsOnUnresolved() throws Exception {
		TemplateImmediateExpression result = interpreter.visitExists(new Exists(new EvalContextVar("unresolvable")), scope);

		assertThat(result, equalTo(FALSE));
	}

	@Test
	public void testVisitExistsOnVariableResolutionError() throws Exception {
		VariableResolutionError error = new VariableResolutionError(null, scope);
		when(handler.handle(any(ErrorExpression.class))).thenReturn(error);
		
		TemplateImmediateExpression result = interpreter.visitExists(new Exists(error), scope);

		assertThat(result, equalTo(FALSE));
	}

	@Test
	public void testVisitExistsOnExpressionResolutionError() throws Exception {
		ExpressionResolutionError error = new ExpressionResolutionError(null, null, null, scope, null);
		when(handler.handle(any(ErrorExpression.class))).thenReturn(error);
		
		TemplateImmediateExpression result = interpreter.visitExists(new Exists(error), scope);

		assertThat(result, equalTo(FALSE));
	}

	@Test
	public void testVisitExistsOnStaticUnresolved() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		TemplateImmediateExpression result = interpreter.visitExists(new Exists(new EvalVar("unresolvable", definition)), scope);

		assertThat(result, equalTo(FALSE));
	}

	@Test
	public void testVisitDefaultedOnConstant() throws Exception {
		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(string("exists"), string("default")), scope);

		assertThat(result, equalTo(string("exists")));
	}

	@Test
	public void testVisitDefaultedOnResolved() throws Exception {
		when(scope.resolveContextVariable("resolvable")).thenReturn(var("resolvable", string("exists")));
		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(new EvalContextVar("resolvable"), string("default")), scope);

		assertThat(result, equalTo(string("exists")));
	}

	@Test
	public void testVisitDefaultedOnUnresolved() throws Exception {
		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(new EvalContextVar("unresolvable"), string("default")), scope);

		assertThat(result, equalTo(string("default")));
	}

	@Test
	public void testVisitDefaultedOnVariableResolutionError() throws Exception {
		VariableResolutionError error = new VariableResolutionError(null, scope);
		when(handler.handle(any(ErrorExpression.class))).thenReturn(error);
		
		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(error, string("default")), scope);
		
		assertThat(result, equalTo(string("default")));
	}
	
	@Test
	public void testVisitDefaultedOnExpressionResolutionError() throws Exception {
		ExpressionResolutionError error = new ExpressionResolutionError(null, null, null, scope, null);
		when(handler.handle(any(ErrorExpression.class))).thenReturn(error);
		
		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(error, string("default")), scope);
		
		assertThat(result, equalTo(string("default")));
	}
	
	@Test
	public void testVisitDefaultedOnStaticUnresolved() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(new EvalVar("unresolvable", definition), string("default")), scope);

		assertThat(result, equalTo(string("default")));
	}

	@Test
	public void testVisitConcatElements() throws Exception {
		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(string("1"), string("2")), scope);

		assertThat(result, equalTo(new ResolvedListLiteral(string("1"), string("2"))));
	}

	@Test
	public void testVisitConcatLists() throws Exception {
		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(list(string("1"), string("2")), list(string("3"), string("4"))), scope);

		assertThat(result, equalTo(new ResolvedListLiteral(string("1"), string("2"), string("3"), string("4"))));
	}

	@Test
	public void testVisitConcatMixedElementFirst() throws Exception {
		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(string("1"), list(string("2"), string("3"))), scope);

		assertThat(result, equalTo(new ResolvedListLiteral(string("1"), string("2"), string("3"))));
	}

	@Test
	public void testVisitConcatMixedElementLast() throws Exception {
		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(list(string("1"), string("2")), string("3")), scope);

		assertThat(result, equalTo(new ResolvedListLiteral(string("1"), string("2"), string("3"))));
	}

	@Test
	public void testVisitConcatMaps() throws Exception {
		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(map(var("a", string("1"))), map(var("b", string("2")))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("a", string("1")), var("b", string("2")))));
	}

	@Test
	public void testVisitConcatMapsWithDuplicates() throws Exception {
		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(map(var("a", string("1"))), map(var("a", string("2")))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("a", string("2")))));
	}

	@Test
	public void testVisitConcatObjects() throws Exception {
		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(map(var("_type", string("type1"))), map(var("_type", string("type2")))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("_supertypes", new ResolvedListLiteral(string("type1"), string("type2"))))));
	}

	@Test
	public void testVisitToObjectOnScalar() throws Exception {
		TemplateImmediateExpression result = interpreter.visitToObject(new ToObject("mytype", string("s")), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("_type", string("mytype")), var("_value", string("s")))));
	}

	@Test
	public void testVisitToObjectOnMap() throws Exception {
		TemplateImmediateExpression result = interpreter.visitToObject(new ToObject("mytype", map(var("a", integer(1)), var("b", integer(2)))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("_type", string("mytype")), var("a", integer(1)), var("b", integer(2)))));
	}

	@Test
	public void testVisitToObjectOnObject() throws Exception {
		TemplateImmediateExpression result = interpreter.visitToObject(new ToObject("mytype", map(var("a", integer(1)), var("_type", string("superclass")))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("_type", string("mytype")), var("_supertypes", new ResolvedListLiteral(string("superclass"))), var("a", integer(1)))));
	}

	@Test
	public void testVisitMapLiteral() throws Exception {
		TemplateImmediateExpression result = interpreter.visitMapLiteral(map(var("a", string("0")), var("b", string("2")), var("a", string("1"))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("a", string("1")), var("b", string("2")))));
	}

	@Test
	public void testVisitResolvedMapLiteral() throws Exception {
		ResolvedMapLiteral map = new ResolvedMapLiteral(var("a", string("1")), var("b", string("2")));
		TemplateImmediateExpression result = interpreter.visitMapLiteral(map, scope);

		assertThat(result, sameInstance(map));
	}

	@Test
	public void testVisitListLiteral() throws Exception {
		TemplateImmediateExpression result = interpreter.visitListLiteral(list(string("x"), string("y")), scope);

		assertThat(result, equalTo(new ResolvedListLiteral(string("x"), string("y"))));
	}

	@Test
	public void testVisitResolvedListLiteral() throws Exception {
		ResolvedListLiteral list = new ResolvedListLiteral(string("u"), string("v"));
		TemplateImmediateExpression result = interpreter.visitListLiteral(list, scope);

		assertThat(result, sameInstance(list));
	}

	@Test
	public void testVisitStringLiteral() throws Exception {
		StringLiteral literal = string("s");
		TemplateImmediateExpression result = interpreter.visitStringLiteral(literal, scope);

		assertThat(result, sameInstance(literal));
	}

	@Test
	public void testVisitIntegerLiteral() throws Exception {
		IntegerLiteral literal = integer(1);
		TemplateImmediateExpression result = interpreter.visitIntegerLiteral(literal, scope);

		assertThat(result, sameInstance(literal));
	}

	@Test
	public void testVisitDecimalLiteral() throws Exception {
		DecimalLiteral literal = decimal(-0.3);
		TemplateImmediateExpression result = interpreter.visitDecimalLiteral(literal, scope);

		assertThat(result, sameInstance(literal));
	}

	@Test
	public void testVisitBooleanLiteral() throws Exception {
		assertThat(interpreter.visitBooleanLiteral(FALSE, scope), sameInstance(FALSE));
		assertThat(interpreter.visitBooleanLiteral(TRUE, scope), sameInstance(TRUE));
	}

	@Test
	public void testVisitNativeObject() throws Exception {
		NativeObject literal = new NativeObject(this);
		TemplateImmediateExpression result = interpreter.visitNativeObject(literal, scope);

		assertThat(result, sameInstance(literal));
	}

	@Test
	public void testVisitCastNotAcceptsPrimitives() throws Exception {
		TemplateImmediateExpression result = interpreter.visitCast(new Cast(string("str"), "object"), scope);
		assertThat(result, instanceOf(UnexpectedTypeError.class));
	}

	@Test
	public void testVisitCastNotAcceptsMaps() throws Exception {
		ResolvedMapLiteral map = new ResolvedMapLiteral(var("a", string("b")));
		TemplateImmediateExpression result = interpreter.visitCast(new Cast(map, "object"), scope);
		assertThat(result, instanceOf(UnexpectedTypeError.class));
	}

	@Test
	public void testVisitCastAcceptsObjectsWithType() throws Exception {
		ResolvedMapLiteral map = new ResolvedMapLiteral(var("a", string("b")),var("_type", string("object")));
		TemplateImmediateExpression result = interpreter.visitCast(new Cast(map, "object"), scope);
		assertThat(result, sameInstance(map));
	}

	@Test
	public void testVisitCastAcceptsObjectsWithSuperType() throws Exception {
		ResolvedMapLiteral map = new ResolvedMapLiteral(var("a", string("b")),var("_supertypes", new ResolvedListLiteral(string("object"))));
		TemplateImmediateExpression result = interpreter.visitCast(new Cast(map, "object"), scope);
		assertThat(result, sameInstance(map));
	}

	@Test
	public void testVisitErrorExpression() throws Exception {
		ErrorExpression error = new ErrorExpression() {

			@Override
			public String getMessage() {
				return "error";
			}
		};

		interpreter.visitErrorExpression(error, scope);

		verify(handler).handle(error);
	}

	@Test
	public void testSuperTypesOfType() throws Exception {
		List<TemplateImmediateExpression> superTypes = interpreter.superTypes(new ResolvedMapLiteral(
			var("_type", string("nextsupertype"))))
			.collect(toList());
		assertThat(superTypes, contains(string("nextsupertype")));
	}

	@Test
	public void testSuperTypesOfSuperType() throws Exception {
		List<TemplateImmediateExpression> superTypes = interpreter.superTypes(new ResolvedMapLiteral(
			var("_supertypes", string("supertype"))))
			.collect(toList());
		assertThat(superTypes, contains(string("supertype")));
	}

	@Test
	public void testSuperTypesOfSuperTypes() throws Exception {
		List<TemplateImmediateExpression> superTypes = interpreter.superTypes(new ResolvedMapLiteral(
			var("_supertypes", new ResolvedListLiteral(string("supertype1"), string("supertype2")))))
			.collect(toList());
		assertThat(superTypes, contains(string("supertype1"), string("supertype2")));
	}

	@Test
	public void testSuperTypesOfTypeAndSuperTypes() throws Exception {
		List<TemplateImmediateExpression> superTypes = interpreter.superTypes(new ResolvedMapLiteral(
			var("_type", string("type")),
			var("_supertypes", new ResolvedListLiteral(string("supertype1"), string("supertype2")))))
			.collect(toList());
		assertThat(superTypes, contains(string("type"), string("supertype1"), string("supertype2")));
	}

}
