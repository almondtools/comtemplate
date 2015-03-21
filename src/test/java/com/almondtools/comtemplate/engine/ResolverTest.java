package com.almondtools.comtemplate.engine;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;

public class ResolverTest {

	@Test
	public void testResolve() throws Exception {
		assertThat(Resolver.NULL.resolve(null, null, null, null), instanceOf(ExpressionResolutionError.class));
	}

}
