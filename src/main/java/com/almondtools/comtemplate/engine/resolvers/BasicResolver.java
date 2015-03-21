package com.almondtools.comtemplate.engine.resolvers;



public class BasicResolver extends CompoundResolver {

	public BasicResolver() {
		add(new TrimResolver());
		add(new CompressResolver());
		add(new EmptyResolver());
	}

}
