package com.almondtools.comtemplate.engine;

public interface InterpreterListener {

	void notify(TemplateExpression source, TemplateImmediateExpression result);

}
