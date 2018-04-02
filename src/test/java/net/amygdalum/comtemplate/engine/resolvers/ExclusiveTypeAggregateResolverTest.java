package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.StringLiteral;
import net.amygdalum.comtemplate.engine.resolvers.ExclusiveTypeAggregateResolver;
import net.amygdalum.comtemplate.engine.resolvers.ExclusiveTypeFunctionResolver;


public class ExclusiveTypeAggregateResolverTest {

	private ExclusiveTypeAggregateResolver<StringLiteral> resolver;

	@BeforeEach
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
