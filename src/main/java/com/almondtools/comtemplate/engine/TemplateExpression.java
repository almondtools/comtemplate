package com.almondtools.comtemplate.engine;


public interface TemplateExpression {

	<T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope);

}
