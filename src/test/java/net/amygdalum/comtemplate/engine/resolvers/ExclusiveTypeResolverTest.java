package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.IntegerLiteral;
import net.amygdalum.comtemplate.engine.expressions.StringLiteral;

public class ExclusiveTypeResolverTest {

	private ExclusiveTypeResolver<StringLiteral> resolver;

	@BeforeEach
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
