package net.amygdalum.comtemplate.engine.resolvers;

import java.util.Arrays;

public class TestPolymorphousAdaptor extends PolymorphousAdaptor {
	TestPolymorphousAdaptor(String name, String... types) {
		super(name, Arrays.stream(types).map(TestAdaptor::new).toArray(MonomophousAdaptor[]::new));
	}
}