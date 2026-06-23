package net.amygdalum.comtemplate.engine.resolvers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class MonomophousAdaptorTest {

	@Test
	public void testMonomophousAdaptor() throws Exception {
		assertThat(new TestAdaptor("type").getType(), equalTo("type"));
	}

}
