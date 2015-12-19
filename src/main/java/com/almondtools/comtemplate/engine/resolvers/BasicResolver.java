package com.almondtools.comtemplate.engine.resolvers;

import com.almondtools.comtemplate.engine.TemplateImmediateExpression;



public class BasicResolver extends CompoundResolver {

	public BasicResolver() {
		super(TemplateImmediateExpression.class, 
			new TrimResolver(),
			new IndentResolver(),
			new CompressResolver(),
			new EmptyResolver());
	}

}
