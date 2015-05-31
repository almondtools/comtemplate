package com.almondtools.comtemplate.engine;

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
import org.mockito.runners.MockitoJUnitRunner;

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
import com.almondtools.comtemplate.engine.expressions.ToObject;


@RunWith(MockitoJUnitRunner.class)
public class TemplateEventNotifierTest {

	@Mock
	private Scope scope;
	@Mock
	private InterpreterListener listener;
	
	private TemplateEventNotifier notifier;

	@Before
	public void before() throws Exception {
		notifier = new TemplateEventNotifier();
		notifier.addListener(listener);
	}
	
	@Test
	public void testVisitRawText() throws Exception {
		verifyListenerIsNotified(notifier, new RawText("raw text"));
	}

	@Test
	public void testVisitEvalVar() throws Exception {
		verifyListenerIsNotified(notifier, new EvalVar("var", mock(TemplateDefinition.class)));
	}

	@Test
	public void testVisitEvalContextVar() throws Exception {
		verifyListenerIsNotified(notifier, new EvalContextVar("var"));
	}

	@Test
	public void testVisitEvalTemplate() throws Exception {
		verifyListenerIsNotified(notifier, new EvalTemplate("template", mock(TemplateDefinition.class)));
	}

	@Test
	public void testVisitEvalTemplateFunction() throws Exception {
		verifyListenerIsNotified(notifier, new EvalTemplateFunction("template", mock(TemplateDefinition.class)));
	}

	@Test
	public void testVisitEvalAnonymousTemplate() throws Exception {
		verifyListenerIsNotified(notifier, new EvalAnonymousTemplate(mock(TemplateDefinition.class)));
	}

	@Test
	public void testVisitEvalAttribute() throws Exception {
		verifyListenerIsNotified(notifier, new EvalAttribute(string("base"),"att"));
	}

	@Test
	public void testVisitEvalFunction() throws Exception {
		verifyListenerIsNotified(notifier, new EvalFunction(string("base"),"func"));
	}

	@Test
	public void testVisitEvaluated() throws Exception {
		verifyListenerIsNotified(notifier, new Evaluated());
	}

	@Test
	public void testVisitExists() throws Exception {
		verifyListenerIsNotified(notifier, new Exists(mock(TemplateExpression.class)));
	}

	@Test
	public void testVisitDefaulted() throws Exception {
		verifyListenerIsNotified(notifier, new Defaulted(string("a"), string("b")));
	}

	@Test
	public void testVisitConcat() throws Exception {
		verifyListenerIsNotified(notifier, new Concat());
	}

	@Test
	public void testVisitToObject() throws Exception {
		verifyListenerIsNotified(notifier, new ToObject("type", mock(TemplateExpression.class)));
	}

	@Test
	public void testVisitMapLiteralMapLiteralScope() throws Exception {
		verifyListenerIsNotified(notifier, map());
	}

	@Test
	public void testVisitMapLiteralResolvedMapLiteralScope() throws Exception {
		verifyListenerIsNotified(notifier, new ResolvedMapLiteral());
	}

	@Test
	public void testVisitListLiteralListLiteralScope() throws Exception {
		verifyListenerIsNotified(notifier, list());
	}

	@Test
	public void testVisitListLiteralResolvedListLiteralScope() throws Exception {
		verifyListenerIsNotified(notifier, new ResolvedListLiteral());
	}

	@Test
	public void testVisitStringLiteral() throws Exception {
		verifyListenerIsNotified(notifier, string("s"));
	}

	@Test
	public void testVisitIntegerLiteral() throws Exception {
		verifyListenerIsNotified(notifier, integer(1));
	}

	@Test
	public void testVisitDecimalLiteral() throws Exception {
		verifyListenerIsNotified(notifier, decimal(1.2));
	}

	@Test
	public void testVisitBooleanLiteral() throws Exception {
		verifyListenerIsNotified(notifier, TRUE);
	}

	@Test
	public void testVisitNativeObject() throws Exception {
		verifyListenerIsNotified(notifier, new NativeObject(new Object()));
	}

	@Test
	public void testVisitCast() throws Exception {
		verifyListenerIsNotified(notifier, new Cast(mock(TemplateExpression.class), "to"));
	}

	@Test
	public void testVisitErrorExpression() throws Exception {
		verifyListenerIsNotified(notifier, new ErrorExpression() {
			
			@Override
			public String getMessage() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	public void verifyListenerIsNotified(TemplateEventNotifier notifier, TemplateExpression source) {
		TemplateImmediateExpression result = source.apply(notifier, scope);;
		
		verify(listener).notify(source, result);
	}

}
