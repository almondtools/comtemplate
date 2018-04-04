package net.amygdalum.comtemplate.engine.resolvers;

import static com.almondtools.conmatch.conventions.UtilityClassMatcher.isUtilityClass;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;


public class NormalizationsTest {

	@Test
	public void testNormalizations() throws Exception {
		assertThat(Normalizations.class, isUtilityClass());
	}
}
