package net.amygdalum.comtemplate.engine;

public interface InterpreterListener {

	void notify(Scope scope, TemplateExpression source, TemplateImmediateExpression result);

}
