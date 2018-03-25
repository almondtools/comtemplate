package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.GlobalTemplates.defaultTemplates;
import static com.almondtools.comtemplate.engine.ResolverRegistry.defaultRegistry;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.ListLiteral.list;
import static com.almondtools.comtemplate.engine.expressions.MapLiteral.map;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.almondtools.comtemplate.engine.expressions.Cast;
import com.almondtools.comtemplate.engine.expressions.Concat;
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
import com.almondtools.comtemplate.engine.expressions.NativeObject;
import com.almondtools.comtemplate.engine.expressions.RawText;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;
import com.almondtools.comtemplate.engine.expressions.TestError;
import com.almondtools.comtemplate.engine.expressions.ToObject;

@RunWith(MockitoJUnitRunner.class)
public class TemplateEventNotifierTest {

	@Mock
	private InterpreterListener listener;

	private TemplateEventNotifier notifier;

	@Before
	public void before() throws Exception {
		notifier = new TemplateEventNotifier(defaultRegistry(), defaultTemplates(), new DefaultErrorHandler());
		notifier.addListener(listener);
	}

	@Test
	public void testTemplateEventNotifierHandlingErrors() throws Exception {
		ErrorHandler errors = mock(ErrorHandler.class);
		Scope scope = mock(Scope.class);
		TestError error = new TestError("test");
		notifier = new TemplateEventNotifier(new ResolverRegistry(), defaultTemplates(), errors);

		notifier.visitErrorExpression(error, scope);

		verify(errors).clear(error, error);
	}

	@Test
	public void testVisitRawText() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new RawText("raw text"));
	}

	@Test
	public void testVisitEvalVar() throws Exception {
		TestScope scope = new TestScope();
		verifyListenerIsNotified(notifier, scope, new EvalVar("var", scope.getDefinition()));
	}

	@Test
	public void testVisitEvalContextVar() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new EvalContextVar("var"));
	}

	@Test
	public void testVisitEvalTemplate() throws Exception {
		TestScope scope = new TestScope() {
			@Override
			public TemplateDefinition resolveTemplate(String template, TemplateDefinition definition) {
				return new TestTemplateDefinition("test");
			}
		};
		verifyListenerIsNotified(notifier, scope, new EvalTemplate("template", scope.getDefinition()));
	}

	@Test
	public void testVisitEvalTemplateFunction() throws Exception {
		TestScope scope = new TestScope() {
			@Override
			public TemplateDefinition resolveTemplate(String template, TemplateDefinition definition) {
				return new TestTemplateDefinition("test");
			}
		};
		verifyListenerIsNotified(notifier, scope, new EvalTemplateFunction("template", scope.getDefinition()));
	}

	@Test
	public void testVisitEvalAnonymousTemplate() throws Exception {
		TestScope scope = new TestScope();
		verifyListenerIsNotified(notifier, scope, new EvalAnonymousTemplate(scope.getDefinition()));
	}

	@Test
	public void testVisitEvalAttribute() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new EvalAttribute(string("base"), "att"));
	}

	@Test
	public void testVisitEvalFunction() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new EvalFunction(string("base"), "func"));
	}

	@Test
	public void testVisitEvaluated() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new Evaluated());
	}

	@Test
	public void testVisitExists() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new Exists(mock(TemplateExpression.class)));
	}

	@Test
	public void testVisitDefaulted() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new Defaulted(string("a"), string("b")));
	}

	@Test
	public void testVisitConcat() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new Concat());
	}

	@Test
	public void testVisitToObject() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new ToObject("type", mock(TemplateExpression.class)));
	}

	@Test
	public void testVisitMapLiteralMapLiteralScope() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), map());
	}

	@Test
	public void testVisitMapLiteralResolvedMapLiteralScope() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new ResolvedMapLiteral());
	}

	@Test
	public void testVisitListLiteralListLiteralScope() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), list());
	}

	@Test
	public void testVisitListLiteralResolvedListLiteralScope() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new ResolvedListLiteral());
	}

	@Test
	public void testVisitStringLiteral() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), string("s"));
	}

	@Test
	public void testVisitIntegerLiteral() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), integer(1));
	}

	@Test
	public void testVisitDecimalLiteral() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), decimal(1.2));
	}

	@Test
	public void testVisitBooleanLiteral() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), TRUE);
	}

	@Test
	public void testVisitNativeObject() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new NativeObject(new Object()));
	}

	@Test
	public void testVisitCast() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new Cast(mock(TemplateExpression.class), "to"));
	}

	@Test
	public void testVisitErrorExpression() throws Exception {
		verifyListenerIsNotified(notifier, new TestScope(), new ErrorExpression() {

			@Override
			public String getMessage() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	public void verifyListenerIsNotified(TemplateEventNotifier notifier, Scope scope, TemplateExpression source) {
		TemplateImmediateExpression result = source.apply(notifier, scope);

		verify(listener).notify(source, result);
	}

}
