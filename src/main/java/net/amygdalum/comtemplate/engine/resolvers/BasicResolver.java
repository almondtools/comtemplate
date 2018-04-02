package net.amygdalum.comtemplate.engine.resolvers;

import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;



public class BasicResolver extends CompoundResolver {

	public BasicResolver() {
		super(TemplateImmediateExpression.class, 
			new TrimResolver(),
			new IndentResolver(),
			new CompressResolver(),
			new EmptyResolver());
	}

}
