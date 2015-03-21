package com.almondtools.comtemplate.engine;

public interface TemplateImmediateExpression extends TemplateExpression {

	String getText();

	<T> T as(Class<T> clazz);

}
