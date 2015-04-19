package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.StringLiteral;


public class ExclusiveTypeAggregateResolverTest {

	private ExclusiveTypeAggregateResolver<StringLiteral> resolver;

	@Before
	public void before() throws Exception {
		ExclusiveTypeFunctionResolver<StringLiteral> functionResolver = new ExclusiveTypeFunctionResolver<StringLiteral>(StringLiteral.class, "func") {
			
			@Override
			public TemplateImmediateExpression resolveTyped(StringLiteral base, List<TemplateImmediateExpression> arguments, Scope scope) {
				return TRUE;
			}
		};
		resolver = new ExclusiveTypeAggregateResolver<StringLiteral>(StringLiteral.class, functionResolver);
	}
	
	@Test
	public void testResolveTypedMatching() throws Exception {
		Scope scope = mock(Scope.class);
		
		assertThat(resolver.resolve(string("base"), "func", emptyList(), scope).as(Boolean.class), is(true));
	}

	@Test
	public void testResolveTypedNotMatchingFunction() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(string("base"), "nofunc", emptyList(), scope), instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveTypedNotMatchingType() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(TRUE, "func", emptyList(), scope), instanceOf(ExpressionResolutionError.class));
	}
	
}
