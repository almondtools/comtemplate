package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.StringLiteral;

public class ExclusiveTypeFunctionResolverTest {

	private ExclusiveTypeFunctionResolver<StringLiteral> resolver;

	@BeforeEach
	public void before() {
		resolver = new ExclusiveTypeFunctionResolver<StringLiteral>(StringLiteral.class, "func", 2) {

			@Override
			public TemplateImmediateExpression resolveTyped(StringLiteral base, List<TemplateImmediateExpression> arguments, Scope scope) {
				return TRUE;
			}

		};
	}

	@Test
	public void testGetName() throws Exception {
		assertThat(resolver.getName(), equalTo("func"));
	}

	@Test
	public void testGetArity() throws Exception {
		assertThat(resolver.getArity(), equalTo(2));
	}

	@Test
	public void testResolveMatching() throws Exception {
		Scope scope = mock(Scope.class);

		TemplateImmediateExpression resolved = resolver.resolve(string("str"), "func", asList(integer(1), integer(2)), scope);

		assertThat(resolved, equalTo(TRUE));
	}

	@Test
	public void testResolveNotMatchingName() throws Exception {
		Scope scope = mock(Scope.class);

		TemplateImmediateExpression resolved = resolver.resolve(string("str"), "notfunc", asList(integer(1), integer(2)), scope);

		assertThat(resolved, instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveNotMatchingArity() throws Exception {
		Scope scope = mock(Scope.class);

		TemplateImmediateExpression resolved = resolver.resolve(string("str"), "func", asList(integer(0)), scope);

		assertThat(resolved, instanceOf(ExpressionResolutionError.class));
	}

}
