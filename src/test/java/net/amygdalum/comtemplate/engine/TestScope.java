package net.amygdalum.comtemplate.engine;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateVariable;

public class TestScope extends Scope {

	public TestScope() {
		this(new TestTemplateDefinition("test"));
	}

	public TestScope(TemplateDefinition definition, TemplateVariable... variables) {
		super(definition, variables);
	}

}
