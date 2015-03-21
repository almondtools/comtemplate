package com.almondtools.comtemplate.parser;


public class TemplateGroupNode {

	private Object payload;

	public TemplateGroupNode(Object payload) {
		this.payload = payload;
	}

	public <T> T as(Class<T> clazz) {
		if (clazz.isInstance(payload)) {
			return clazz.cast(payload);
		}
		return null;
	}

}
