package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;
import static net.amygdalum.comtemplate.engine.expressions.BooleanLiteral.TRUE;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.resolvers.FunctionResolver;

public class FunctionResolverTest {

	private FunctionResolver resolver;

	@BeforeEach
	public void before() {
		resolver = new FunctionResolver("func", 2) {

			@Override
			public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
				return TRUE;
			}
			
			@Override
			public List<Class<? extends TemplateImmediateExpression>> getResolvedClasses() {
				return asList(TemplateImmediateExpression.class);
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
