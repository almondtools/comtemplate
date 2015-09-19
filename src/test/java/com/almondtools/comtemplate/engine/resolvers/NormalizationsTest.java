package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.util.objects.UtilityClassMatcher.isUtilityClass;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class NormalizationsTest {

	@Test
	public void testNormalizations() throws Exception {
		assertThat(Normalizations.class, isUtilityClass());
	}
}
