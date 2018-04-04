package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.ResolvedMapLiteral;

public class MapDynamicResolverTest {

	private MapDynamicResolver resolver;

	@BeforeEach
	public void before() throws Exception {
		resolver = new MapDynamicResolver();
	}

	@Test
	public void testResolveAttribute() throws Exception {
		Scope scope = mock(Scope.class);
		Map<String, TemplateImmediateExpression> map = new HashMap<>();
		map.put("attribute", string("value"));

		assertThat(resolver.resolveTyped(new ResolvedMapLiteral(map), "attribute", emptyList(), scope), equalTo(string("value")));
	}

	@Test
	public void testResolveAttributeNotFound() throws Exception {
		Scope scope = mock(Scope.class);
		Map<String, TemplateImmediateExpression> map = new HashMap<>();

		assertThat(resolver.resolveTyped(new ResolvedMapLiteral(map), "attribute", emptyList(), scope), instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveSignatureFails() throws Exception {
		Scope scope = mock(Scope.class);
		Map<String, TemplateImmediateExpression> map = new HashMap<>();

		assertThat(resolver.resolveTyped(new ResolvedMapLiteral(map), "attribute", asList(string("")), scope), instanceOf(ExpressionResolutionError.class));
	}

}
