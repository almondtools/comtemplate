package net.amygdalum.comtemplate.engine.expressions;

import static net.amygdalum.comtemplate.engine.expressions.ListLiteral.list;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;
import net.amygdalum.comtemplate.engine.expressions.EvalAttribute;
import net.amygdalum.comtemplate.engine.expressions.NativeObject;

public class EvalAttributeTest {

	@Test
	public void testGetBase() throws Exception {
		assertThat(new EvalAttribute(new NativeObject("str"), "att").getBase(), equalTo(new NativeObject("str")));
	}

	@Test
	public void testGetAttribute() throws Exception {
		assertThat(new EvalAttribute(new NativeObject("str"), "att").getAttribute(), equalTo("att"));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		EvalAttribute eval = new EvalAttribute(list(string("str")), "length");

		eval.apply(visitor, scope);

		verify(visitor).visitEvalAttribute(eval, scope);
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new EvalAttribute(string("str"), "length").toString(), equalTo("'str'.length"));
	}

}
