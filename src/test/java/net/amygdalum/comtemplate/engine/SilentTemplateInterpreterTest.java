package net.amygdalum.comtemplate.engine;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static net.amygdalum.comtemplate.engine.TemplateParameter.param;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.FALSE;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static net.amygdalum.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static net.amygdalum.comtemplate.engine.expressions.ListLiteral.list;
import static net.amygdalum.comtemplate.engine.expressions.MapLiteral.map;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.IntegerLiteral;
import net.amygdalum.comtemplate.engine.expressions.NativeObject;
import net.amygdalum.comtemplate.engine.expressions.RawText;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;
import net.amygdalum.comtemplate.engine.expressions.ResolvedMapLiteral;
import net.amygdalum.comtemplate.engine.expressions.StringLiteral;
import net.amygdalum.comtemplate.engine.expressions.TemplateResolutionError;
import net.amygdalum.comtemplate.engine.expressions.ToObject;
import net.amygdalum.comtemplate.engine.expressions.UnexpectedTypeError;
import net.amygdalum.comtemplate.engine.expressions.VariableResolutionError;

public class SilentTemplateInterpreterTest {

	private TemplateLoader loader;
	private GlobalTemplates globals;
	private ResolverRegistry resolvers;
	private ErrorHandler handler = new DefaultErrorHandler();

	private SilentTemplateInterpreter interpreter;

	@BeforeEach
	public void before() throws Exception {
		loader = Mockito.mock(TemplateLoader.class);
		globals = Mockito.mock(GlobalTemplates.class);
		resolvers = Mockito.mock(ResolverRegistry.class);
		handler = Mockito.spy(new DefaultErrorHandler());
		interpreter = new SilentTemplateInterpreter(loader, resolvers, globals, handler);
	}

	@Test
	public void testVisitRawText() throws Exception {
		RawText rawText = new RawText("raw text");
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitRawText(rawText, scope);

		assertThat(result, sameInstance(rawText));
	}

	@Test
	public void testVisitEvalVarSuccessful() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope(definition, var("var", string("string")));

		TemplateImmediateExpression result = interpreter.visitEvalVar(new EvalVar("var", definition), scope);

		assertThat(result, equalTo(string("string")));
	}

	@Test
	public void testVisitEvalVarFailed() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope(definition);

		TemplateImmediateExpression result = interpreter.visitEvalVar(new EvalVar("var", definition), scope);

		assertThat(result, instanceOf(VariableResolutionError.class));
	}

	@Test
	public void testVisitEvalContextVarGlobal() throws Exception {
		Scope scope = new TestScope();
		when(globals.resolveGlobal("var")).thenReturn(Optional.of(var("var", integer(22))));

		TemplateImmediateExpression result = interpreter.visitEvalContextVar(new EvalContextVar("var"), scope);

		assertThat(result, equalTo(integer(22)));
	}

	@Test
	public void testVisitEvalContextVarSuccesful() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope(definition, var("var", integer(22)));

		TemplateImmediateExpression result = interpreter.visitEvalContextVar(new EvalContextVar("var"), scope);

		assertThat(result, equalTo(integer(22)));
	}

	@Test
	public void testVisitEvalContextVarFailed() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitEvalContextVar(new EvalContextVar("var"), scope);

		assertThat(result, instanceOf(VariableResolutionError.class));
	}

	@Test
	public void testVisitEvalTemplateResolvedGlobally() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope();
		when(globals.resolve("template")).thenReturn(new TestTemplateDefinition("template", param("param")));

		TemplateImmediateExpression result = interpreter.visitEvalTemplate(new EvalTemplate("template", definition, var("param", integer(2))), scope);

		assertThat(result, equalTo(string("test: param=2")));
	}

	@Test
	public void testVisitEvalTemplateResolvedAbsolute() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope();
		when(loader.loadDefinition("absolute.template")).thenReturn(new TestTemplateDefinition("template", param("paramabs")));

		TemplateImmediateExpression result = interpreter.visitEvalTemplate(new EvalTemplate("absolute.template", definition, var("paramabs", integer(2))), scope);

		assertThat(result, equalTo(string("test: paramabs=2")));
	}

	@Test
	public void testVisitEvalTemplateResolvedInScope() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope() {
			@Override
			public TemplateDefinition resolveTemplate(String template, TemplateDefinition def) {
				if (template.equals("template") && def == definition) {
					return new TestTemplateDefinition("template", param("param"));
				}
				return super.resolveTemplate(template, definition);
			}
		};

		TemplateImmediateExpression result = interpreter.visitEvalTemplate(new EvalTemplate("template", definition, var("param", integer(3))), scope);

		assertThat(result, equalTo(string("test: param=3")));
	}

	@Test
	public void testVisitEvalTemplateNotResolved() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		when(definition.getGroup()).thenReturn(new TemplateGroup("test", "testresource"));
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitEvalTemplate(new EvalTemplate("template", definition, var("param", integer(4))), scope);

		assertThat(result, instanceOf(TemplateResolutionError.class));
	}

	@Test
	public void testVisitEvalTemplateFunctionResolvedGlobally() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope();
		when(globals.resolve("template")).thenReturn(new TestTemplateDefinition("template", param("param")));

		TemplateImmediateExpression result = interpreter.visitEvalTemplateFunction(new EvalTemplateFunction("template", definition, integer(2)), scope);

		assertThat(result, equalTo(string("test: param=2")));
	}

	@Test
	public void testVisitEvalTemplateFunctionResolvedAbsolute() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope();
		when(loader.loadDefinition("absolute.template")).thenReturn(new TestTemplateDefinition("template", param("paramabs")));

		TemplateImmediateExpression result = interpreter.visitEvalTemplateFunction(new EvalTemplateFunction("absolute.template", definition, integer(2)), scope);

		assertThat(result, equalTo(string("test: paramabs=2")));
	}

	@Test
	public void testVisitEvalTemplateFunctionResolvedInScope() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope() {
			@Override
			public TemplateDefinition resolveTemplate(String template, TemplateDefinition def) {
				if (template.equals("template") && def == definition) {
					return new TestTemplateDefinition("template", param("param"));
				}
				return super.resolveTemplate(template, definition);
			}
		};

		TemplateImmediateExpression result = interpreter.visitEvalTemplateFunction(new EvalTemplateFunction("template", definition, integer(3)), scope);

		assertThat(result, equalTo(string("test: param=3")));
	}

	@Test
	public void testVisitEvalTemplateFunctionNotResolved() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		when(definition.getGroup()).thenReturn(new TemplateGroup("test", "testresource"));
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitEvalTemplateFunction(new EvalTemplateFunction("template", definition, integer(4)), scope);

		assertThat(result, instanceOf(TemplateResolutionError.class));
	}

	@Test
	public void testVisitEvalTemplateMixedResolvedGlobally() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope();
		when(globals.resolve("template")).thenReturn(new TestTemplateDefinition("template", param("param"), param("named")));
		
		EvalTemplateMixed template = new EvalTemplateMixed("template", definition, new TemplateExpression[] { integer(2) }, var("named", integer(3)));
		TemplateImmediateExpression result = interpreter.visitEvalTemplateMixed(template, scope);

		assertThat(result, equalTo(string("test: param=2,named=3")));
	}

	@Test
	public void testVisitEvalTemplateMixedResolvedAbsolute() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope();
		when(loader.loadDefinition("absolute.template")).thenReturn(new TestTemplateDefinition("template", param("paramabs")));
		
		EvalTemplateMixed template = new EvalTemplateMixed("absolute.template", definition, new TemplateExpression[] { integer(2) }, var("named", integer(3)));
		TemplateImmediateExpression result = interpreter.visitEvalTemplateMixed(template, scope);
		
		assertThat(result, equalTo(string("test: paramabs=2,named=3")));
	}
	
	@Test
	public void testVisitEvalTemplateMixedResolvedInScope() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope() {
			@Override
			public TemplateDefinition resolveTemplate(String template, TemplateDefinition def) {
				if (template.equals("template") && def == definition) {
					return new TestTemplateDefinition("template", param("param"), param("named"));
				}
				return super.resolveTemplate(template, definition);
			}
		};
		
		EvalTemplateMixed template = new EvalTemplateMixed("template", definition, new TemplateExpression[] { integer(3) }, var("named", integer(4)));
		TemplateImmediateExpression result = interpreter.visitEvalTemplateMixed(template,scope);

		assertThat(result, equalTo(string("test: param=3,named=4")));
	}

	@Test
	public void testVisitEvalTemplateMixedNotResolved() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		when(definition.getGroup()).thenReturn(new TemplateGroup("test", "testresource"));
		Scope scope = new TestScope();

		EvalTemplateMixed template = new EvalTemplateMixed("template", definition, new TemplateExpression[] { integer(4) });
		TemplateImmediateExpression result = interpreter.visitEvalTemplateMixed(template, scope);

		assertThat(result, instanceOf(TemplateResolutionError.class));
	}

	@Test
	public void testVisitEvalAnonymousTemplate() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitEvalAnonymousTemplate(new EvalAnonymousTemplate(definition, string("test:"), integer(33)), scope);

		assertThat(result.getText(), equalTo("test:33"));
	}

	@Test
	public void testVisitEvalAttribute() throws Exception {
		Scope scope = new TestScope();
		TemplateExpression base = map(var("key", string("value")));
		when(resolvers.getResolverFor(any(ResolvedMapLiteral.class))).thenReturn(new TestResolver(ResolvedMapLiteral.class));

		TemplateImmediateExpression result = interpreter.visitEvalAttribute(new EvalAttribute(base, "attribute"), scope);

		assertThat(result, equalTo(string("[key='value'].attribute()")));
	}

	@Test
	public void testVisitEvalVirtual() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class, RETURNS_DEEP_STUBS);
		Scope scope = new TestScope(definition, var("var", string("key")));
		TemplateExpression base = map(var("key", string("value")));
		when(resolvers.getResolverFor(any(ResolvedMapLiteral.class))).thenReturn(new TestResolver(ResolvedMapLiteral.class));

		TemplateImmediateExpression result = interpreter.visitEvalVirtual(new EvalVirtual(base, new EvalVar("var", definition)), scope);

		assertThat(result, equalTo(string("[key='value'].key()")));
	}

	@Test
	public void testVisitEvalFunction() throws Exception {
		Scope scope = new TestScope();
		TemplateExpression base = map(var("key", string("value")));

		when(resolvers.getResolverFor(Mockito.any(ResolvedMapLiteral.class))).thenReturn(new TestResolver(ResolvedMapLiteral.class));
		TemplateImmediateExpression result = interpreter.visitEvalFunction(new EvalFunction(base, "attribute", integer(1), integer(2)), scope);

		assertThat(result, equalTo(string("[key='value'].attribute(1,2)")));
	}

	@Test
	public void testVisitEvaluated() throws Exception {
		Scope scope = new TestScope();

		Evaluated evaluated = new Evaluated(emptyList());

		TemplateImmediateExpression result = interpreter.visitEvaluated(evaluated, scope);

		assertThat(result, sameInstance(evaluated));
	}

	@Test
	public void testVisitExistsOnConstant() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitExists(new Exists(string("exists")), scope);

		assertThat(result, equalTo(TRUE));
	}

	@Test
	public void testVisitExistsOnResolved() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope(definition, var("resolvable", string("exists")));

		TemplateImmediateExpression result = interpreter.visitExists(new Exists(new EvalContextVar("resolvable")), scope);

		assertThat(result, equalTo(TRUE));
	}

	@Test
	public void testVisitExistsOnUnresolved() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitExists(new Exists(new EvalContextVar("unresolvable")), scope);

		assertThat(result, equalTo(FALSE));
	}

	@Test
	public void testVisitExistsOnVariableResolutionError() throws Exception {
		Scope scope = new TestScope();
		VariableResolutionError error = new VariableResolutionError(null, scope);

		TemplateImmediateExpression result = interpreter.visitExists(new Exists(error), scope);

		assertThat(result, equalTo(FALSE));
	}

	@Test
	public void testVisitExistsOnExpressionResolutionError() throws Exception {
		Scope scope = new TestScope();
		ExpressionResolutionError error = new ExpressionResolutionError(null, null, null, scope, null);

		TemplateImmediateExpression result = interpreter.visitExists(new Exists(error), scope);

		assertThat(result, equalTo(FALSE));
	}

	@Test
	public void testVisitExistsOnStaticUnresolved() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitExists(new Exists(new EvalVar("unresolvable", definition)), scope);

		assertThat(result, equalTo(FALSE));
	}

	@Test
	public void testVisitDefaultedOnConstant() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(string("exists"), string("default")), scope);

		assertThat(result, equalTo(string("exists")));
	}

	@Test
	public void testVisitDefaultedOnResolved() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope(definition, var("resolvable", string("exists")));

		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(new EvalContextVar("resolvable"), string("default")), scope);

		assertThat(result, equalTo(string("exists")));
	}

	@Test
	public void testVisitDefaultedOnUnresolved() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(new EvalContextVar("unresolvable"), string("default")), scope);

		assertThat(result, equalTo(string("default")));
	}

	@Test
	public void testVisitDefaultedOnVariableResolutionError() throws Exception {
		Scope scope = new TestScope();
		VariableResolutionError error = new VariableResolutionError(null, scope);

		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(error, string("default")), scope);

		assertThat(result, equalTo(string("default")));
	}

	@Test
	public void testVisitDefaultedOnExpressionResolutionError() throws Exception {
		Scope scope = new TestScope();
		ExpressionResolutionError error = new ExpressionResolutionError(null, null, null, scope, null);

		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(error, string("default")), scope);

		assertThat(result, equalTo(string("default")));
	}

	@Test
	public void testVisitDefaultedOnStaticUnresolved() throws Exception {
		TemplateDefinition definition = mock(TemplateDefinition.class);
		Scope scope = new TestScope(definition);

		TemplateImmediateExpression result = interpreter.visitDefaulted(new Defaulted(new EvalVar("unresolvable", definition), string("default")), scope);

		assertThat(result, equalTo(string("default")));
	}

	@Test
	public void testVisitConcatElements() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(string("1"), string("2")), scope);

		assertThat(result.getText(), equalTo("12"));
	}

	@Test
	public void testVisitConcatListAndElement() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(list(string("1")), string("2")), scope);

		assertThat(result, equalTo(new ResolvedListLiteral(string("1"), string("2"))));
	}

	@Test
	public void testVisitConcatElementAndList() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(string("1"), list(string("2"))), scope);

		assertThat(result, equalTo(new ResolvedListLiteral(string("1"), string("2"))));
	}

	@Test
	public void testVisitConcatLists() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(list(string("1"), string("2")), list(string("3"), string("4"))), scope);

		assertThat(result, equalTo(new ResolvedListLiteral(string("1"), string("2"), string("3"), string("4"))));
	}

	@Test
	public void testVisitConcatMixedElementFirst() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(string("1"), list(string("2"), string("3"))), scope);

		assertThat(result, equalTo(new ResolvedListLiteral(string("1"), string("2"), string("3"))));
	}

	@Test
	public void testVisitConcatMixedElementLast() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(list(string("1"), string("2")), string("3")), scope);

		assertThat(result, equalTo(new ResolvedListLiteral(string("1"), string("2"), string("3"))));
	}

	@Test
	public void testVisitConcatMaps() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(map(var("a", string("1"))), map(var("b", string("2")))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("a", string("1")), var("b", string("2")))));
	}

	@Test
	public void testVisitConcatMapsWithDuplicates() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(map(var("a", string("1"))), map(var("a", string("2")))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("a", string("2")))));
	}

	@Test
	public void testVisitConcatObjects() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitConcat(new Concat(map(var("_type", string("type1"))), map(var("_type", string("type2")))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("_supertypes", new ResolvedListLiteral(string("type1"), string("type2"))))));
	}

	@Test
	public void testVisitToObjectOnScalar() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitToObject(new ToObject("mytype", string("s")), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("_type", string("mytype")), var("_value", string("s")))));
	}

	@Test
	public void testVisitToObjectOnMap() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitToObject(new ToObject("mytype", map(var("a", integer(1)), var("b", integer(2)))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("_type", string("mytype")), var("a", integer(1)), var("b", integer(2)))));
	}

	@Test
	public void testVisitToObjectOnObject() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitToObject(new ToObject("mytype", map(var("a", integer(1)), var("_type", string("superclass")))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("_type", string("mytype")), var("_supertypes", new ResolvedListLiteral(string("superclass"))), var("a", integer(1)))));
	}

	@Test
	public void testVisitMapLiteral() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitMapLiteral(map(var("a", string("0")), var("b", string("2")), var("a", string("1"))), scope);

		assertThat(result, equalTo(new ResolvedMapLiteral(var("a", string("1")), var("b", string("2")))));
	}

	@Test
	public void testVisitResolvedMapLiteral() throws Exception {
		Scope scope = new TestScope();

		ResolvedMapLiteral map = new ResolvedMapLiteral(var("a", string("1")), var("b", string("2")));
		TemplateImmediateExpression result = interpreter.visitMapLiteral(map, scope);

		assertThat(result, sameInstance(map));
	}

	@Test
	public void testVisitListLiteral() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitListLiteral(list(string("x"), string("y")), scope);

		assertThat(result, equalTo(new ResolvedListLiteral(string("x"), string("y"))));
	}

	@Test
	public void testVisitResolvedListLiteral() throws Exception {
		Scope scope = new TestScope();

		ResolvedListLiteral list = new ResolvedListLiteral(string("u"), string("v"));
		TemplateImmediateExpression result = interpreter.visitListLiteral(list, scope);

		assertThat(result, sameInstance(list));
	}

	@Test
	public void testVisitStringLiteral() throws Exception {
		Scope scope = new TestScope();

		StringLiteral literal = string("s");
		TemplateImmediateExpression result = interpreter.visitStringLiteral(literal, scope);

		assertThat(result, sameInstance(literal));
	}

	@Test
	public void testVisitIntegerLiteral() throws Exception {
		Scope scope = new TestScope();

		IntegerLiteral literal = integer(1);
		TemplateImmediateExpression result = interpreter.visitIntegerLiteral(literal, scope);

		assertThat(result, sameInstance(literal));
	}

	@Test
	public void testVisitDecimalLiteral() throws Exception {
		Scope scope = new TestScope();

		DecimalLiteral literal = decimal(-0.3);
		TemplateImmediateExpression result = interpreter.visitDecimalLiteral(literal, scope);

		assertThat(result, sameInstance(literal));
	}

	@Test
	public void testVisitBooleanLiteral() throws Exception {
		Scope scope = new TestScope();

		assertThat(interpreter.visitBooleanLiteral(FALSE, scope), sameInstance(FALSE));
		assertThat(interpreter.visitBooleanLiteral(TRUE, scope), sameInstance(TRUE));
	}

	@Test
	public void testVisitNativeObject() throws Exception {
		Scope scope = new TestScope();

		NativeObject literal = new NativeObject(this);
		TemplateImmediateExpression result = interpreter.visitNativeObject(literal, scope);

		assertThat(result, sameInstance(literal));
	}

	@Test
	public void testVisitCastNotAcceptsPrimitives() throws Exception {
		Scope scope = new TestScope();

		TemplateImmediateExpression result = interpreter.visitCast(new Cast(string("str"), "object"), scope);

		assertThat(result, instanceOf(UnexpectedTypeError.class));
	}

	@Test
	public void testVisitCastNotAcceptsMaps() throws Exception {
		Scope scope = new TestScope();

		ResolvedMapLiteral map = new ResolvedMapLiteral(var("a", string("b")));

		TemplateImmediateExpression result = interpreter.visitCast(new Cast(map, "object"), scope);

		assertThat(result, instanceOf(UnexpectedTypeError.class));
	}

	@Test
	public void testVisitCastAcceptsObjectsWithType() throws Exception {
		Scope scope = new TestScope();

		ResolvedMapLiteral map = new ResolvedMapLiteral(var("a", string("b")), var("_type", string("object")));

		TemplateImmediateExpression result = interpreter.visitCast(new Cast(map, "object"), scope);

		assertThat(result, sameInstance(map));
	}

	@Test
	public void testVisitCastAcceptsObjectsWithSuperType() throws Exception {
		Scope scope = new TestScope();

		ResolvedMapLiteral map = new ResolvedMapLiteral(var("a", string("b")), var("_supertypes", new ResolvedListLiteral(string("object"))));

		TemplateImmediateExpression result = interpreter.visitCast(new Cast(map, "object"), scope);

		assertThat(result, sameInstance(map));
	}

	@Test
	public void testVisitErrorExpression() throws Exception {
		Scope scope = new TestScope();

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
