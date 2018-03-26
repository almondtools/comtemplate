package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;

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
