package net.amygdalum.comtemplate.engine.resolvers;

import static com.almondtools.conmatch.conventions.UtilityClassMatcher.isUtilityClass;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.resolvers.Normalizations;


public class NormalizationsTest {

	@Test
	public void testNormalizations() throws Exception {
		assertThat(Normalizations.class, isUtilityClass());
	}
}
