package net.amygdalum.comtemplate.engine;

public interface InterpreterListener {

	void notify(TemplateExpression source, TemplateImmediateExpression result);

}
