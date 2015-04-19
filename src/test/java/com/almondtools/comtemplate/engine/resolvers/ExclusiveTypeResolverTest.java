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

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.IntegerLiteral;
import com.almondtools.comtemplate.engine.expressions.StringLiteral;

public class ExclusiveTypeResolverTest {

	private ExclusiveTypeResolver<StringLiteral> resolver;

	@Before
	public void before() {
		resolver = new ExclusiveTypeResolver<StringLiteral>(StringLiteral.class) {

			@Override
			public TemplateImmediateExpression resolveTyped(StringLiteral base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
				return TRUE;
			}
		};
	}

	@Test
	public void testResolveMatching() throws Exception {
		Scope scope = mock(Scope.class);

		TemplateImmediateExpression resolved = resolver.resolve(string("str"), "func", asList(integer(1), integer(2)), scope);

		assertThat(resolved, equalTo(TRUE));
	}

	@Test
	public void testResolveNotMatching() throws Exception {
		Scope scope = mock(Scope.class);

		TemplateImmediateExpression resolved = resolver.resolve(IntegerLiteral.integer(0), "func", asList(integer(1), integer(2)), scope);

		assertThat(resolved, instanceOf(ExpressionResolutionError.class));
	}

}
