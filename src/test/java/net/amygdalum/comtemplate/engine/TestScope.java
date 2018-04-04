package net.amygdalum.comtemplate.engine;

public class TestScope extends Scope {

	public TestScope() {
		this(new TestTemplateDefinition("test"));
	}

	public TestScope(TemplateDefinition definition, TemplateVariable... variables) {
		super(definition, variables);
	}

}
