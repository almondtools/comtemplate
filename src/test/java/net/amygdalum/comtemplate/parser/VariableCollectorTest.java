package net.amygdalum.comtemplate.parser;

import static java.util.Arrays.asList;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static net.amygdalum.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateExpression;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.TemplateVariable;
import net.amygdalum.comtemplate.engine.expressions.Cast;
import net.amygdalum.comtemplate.engine.expressions.Concat;
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
import net.amygdalum.comtemplate.engine.expressions.ListLiteral;
import net.amygdalum.comtemplate.engine.expressions.MapLiteral;
import net.amygdalum.comtemplate.engine.expressions.NativeObject;
import net.amygdalum.comtemplate.engine.expressions.RawText;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;
import net.amygdalum.comtemplate.engine.expressions.ResolvedMapLiteral;
import net.amygdalum.comtemplate.engine.expressions.ToObject;

public class VariableCollectorTest {

	private VariableCollector variableCollector;

	@BeforeEach
	public void before() throws Exception {
		variableCollector = new VariableCollector();
	}

	@Test
	public void testVisitEvalVar() throws Exception {
		variableCollector.visitEvalVar(new EvalVar("evalvar", aDef()), aScope());

		assertThat(variableCollector.getVariables(), contains("evalvar"));
	}

	@Test
	public void testVisitEvalContextVar() throws Exception {
		variableCollector.visitEvalContextVar(new EvalContextVar("contextvar"), aScope());

		assertThat(variableCollector.getVariables(), empty());
	}

	@Test
	public void testVisitEvalTemplate() throws Exception {
		variableCollector.visitEvalTemplate(new EvalTemplate("template", aDef(), text("arg1", "text"), evalVar("arg2", "argumentevalvar")), aScope());

		assertThat(variableCollector.getVariables(), contains("argumentevalvar"));
	}

	@Test
	public void testVisitEvalTemplateFunction() throws Exception {
		variableCollector.visitEvalTemplateFunction(new EvalTemplateFunction("template", aDef(), text("text"), evalVar("argumentevalvar")), aScope());

		assertThat(variableCollector.getVariables(), contains("argumentevalvar"));
	}

	@Test
	public void testVisitEvalTemplateMixed() throws Exception {
		List<TemplateExpression> expressions = asList(text("text"), evalVar("argumentevalvar1"));
		List<TemplateVariable> variables = asList(text("var1", "text"), evalVar("var2", "argumentevalvar2"));
		variableCollector.visitEvalTemplateMixed(new EvalTemplateMixed("template", aDef(), expressions, variables), aScope());

		assertThat(variableCollector.getVariables(), contains("argumentevalvar1", "argumentevalvar2"));
	}

	@Test
	public void testVisitEvalAnonymousTemplate() throws Exception {
		variableCollector.visitEvalAnonymousTemplate(new EvalAnonymousTemplate(aDef(), text("text"), evalVar("argumentevalvar")), aScope());

		assertThat(variableCollector.getVariables(), contains("argumentevalvar"));
	}

	@Test
	public void testVisitEvalAttribute() throws Exception {
		variableCollector.visitEvalAttribute(new EvalAttribute(evalVar("base"), "att"), aScope());

		assertThat(variableCollector.getVariables(), contains("base"));
	}

	@Test
	public void testVisitEvalVirtual() throws Exception {
		variableCollector.visitEvalVirtual(new EvalVirtual(evalVar("base"), evalVar("att")), aScope());

		assertThat(variableCollector.getVariables(), contains("base", "att"));
	}

	@Test
	public void testVisitEvalFunction() throws Exception {
		variableCollector.visitEvalFunction(new EvalFunction(evalVar("base"), "function", text("text"), evalVar("argumentevalvar")), aScope());

		assertThat(variableCollector.getVariables(), contains("base", "argumentevalvar"));
	}

	@Test
	public void testVisitIgnoreErrors() throws Exception {
		variableCollector.visitIgnoreErrors(new IgnoreErrors(evalVar("base")), aScope());

		assertThat(variableCollector.getVariables(), contains("base"));
	}

	@Test
	public void testVisitExists() throws Exception {
		variableCollector.visitExists(new Exists(evalVar("base")), aScope());

		assertThat(variableCollector.getVariables(), contains("base"));
	}

	@Test
	public void testVisitDefaulted() throws Exception {
		variableCollector.visitDefaulted(new Defaulted(evalVar("base"), new EvalVar("def", aDef())), aScope());

		assertThat(variableCollector.getVariables(), contains("base", "def"));
	}

	@Test
	public void testVisitConcat() throws Exception {
		variableCollector.visitConcat(new Concat(evalVar("first"), evalVar("second")), aScope());

		assertThat(variableCollector.getVariables(), contains("first", "second"));
	}

	@Test
	public void testVisitToObject() throws Exception {
		variableCollector.visitToObject(new ToObject("type", evalVar("base")), aScope());

		assertThat(variableCollector.getVariables(), contains("base"));
	}

	@Test
	public void testVisitMapLiteralMapLiteralScope() throws Exception {
		variableCollector.visitMapLiteral(MapLiteral.map(evalVar("key1", "mapvar"), text("key2", "text")), aScope());

		assertThat(variableCollector.getVariables(), contains("mapvar"));
	}

	@Test
	public void testVisitListLiteralListLiteralScope() throws Exception {
		variableCollector.visitListLiteral(ListLiteral.list(evalVar("mapvar"), text("text")), aScope());

		assertThat(variableCollector.getVariables(), contains("mapvar"));
	}

	@Test
	public void testVisitCast() throws Exception {
		variableCollector.visitCast(new Cast(evalVar("base"), "type"), aScope());

		assertThat(variableCollector.getVariables(), contains("base"));
	}

	@Test
	public void testVisitErrorExpression() throws Exception {
		variableCollector.visitErrorExpression(new ErrorExpression() {
			
			@Override
			public String getMessage() {
				return "message";
			}
		}, aScope());

		assertThat(variableCollector.getVariables(), empty());
	}

	@Test
	public void testVisitRawText() throws Exception {
		variableCollector.visitRawText(new RawText("text"), aScope());

		assertThat(variableCollector.getVariables(), empty());
	}

	@Test
	public void testVisitEvaluated() throws Exception {
		variableCollector.visitEvaluated(new Evaluated(text("text")), aScope());

		assertThat(variableCollector.getVariables(), empty());
	}

	@Test
	public void testVisitMapLiteralResolvedMapLiteralScope() throws Exception {
		variableCollector.visitMapLiteral(new ResolvedMapLiteral(text("key", "text")), aScope());

		assertThat(variableCollector.getVariables(), empty());
	}

	@Test
	public void testVisitListLiteralResolvedListLiteralScope() throws Exception {
		variableCollector.visitListLiteral(new ResolvedListLiteral(text("text")), aScope());

		assertThat(variableCollector.getVariables(), empty());
	}

	@Test
	public void testVisitStringLiteral() throws Exception {
		variableCollector.visitStringLiteral(string("string"), aScope());

		assertThat(variableCollector.getVariables(), empty());
	}

	@Test
	public void testVisitIntegerLiteral() throws Exception {
		variableCollector.visitIntegerLiteral(integer(42), aScope());

		assertThat(variableCollector.getVariables(), empty());
	}

	@Test
	public void testVisitDecimalLiteral() throws Exception {
		variableCollector.visitDecimalLiteral(decimal("1.2"), aScope());

		assertThat(variableCollector.getVariables(), empty());
	}

	@Test
	public void testVisitBooleanLiteral() throws Exception {
		variableCollector.visitBooleanLiteral(TRUE, aScope());

		assertThat(variableCollector.getVariables(), empty());
	}

	@Test
	public void testVisitNativeObject() throws Exception {
		variableCollector.visitNativeObject(new NativeObject("string"), aScope());

		assertThat(variableCollector.getVariables(), empty());
	}

	private Scope aScope() {
		return Mockito.mock(Scope.class);
	}

	private TemplateDefinition aDef() {
		return Mockito.mock(TemplateDefinition.class);
	}

	private TemplateVariable evalVar(String name, String var) {
		return var(name, new EvalVar(var, aDef()));
	}

	private TemplateVariable text(String name, String text) {
		return var(name, new RawText(text));
	}

	private TemplateExpression evalVar(String name) {
		return new EvalVar(name, aDef());
	}

	private TemplateImmediateExpression text(String text) {
		return new RawText(text);
	}

}
