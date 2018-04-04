package net.amygdalum.comtemplate.engine.expressions;

import static com.almondtools.conmatch.conventions.EqualityMatcher.satisfiesDefaultEquality;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.bool;
import static net.amygdalum.comtemplate.engine.expressions.MapLiteral.map;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;

public class MapLiteralTest {

	@Test
	public void testGetMap() throws Exception {
		MapLiteral literal = map(var("key", bool(true)));
		assertThat(literal.getMap().keySet(), contains("key"));
		assertThat(literal.getMap().get("key"), sameInstance(TRUE));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		MapLiteral literal = map(var("a", string("a")));

		literal.apply(visitor, scope);

		verify(visitor).visitMapLiteral(literal, scope);
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(map(var("a", string("a"))), satisfiesDefaultEquality()
			.andEqualTo(map(var("a", string("a"))))
			.andEqualTo(map(var("a", string("")),var("a", string("a"))))
			.andNotEqualTo(map(var("a", string("a")),var("a", string(""))))
			.andNotEqualTo(map()));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(map(var("a", string("a"))).toString(), equalTo("[a='a']"));
	}

}
