package com.almondtools.comtemplate.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;

public class UnsupportedLoaderTest {

	private UnsupportedLoader loader;

	@BeforeEach
	public void before() throws Exception {
		loader = new UnsupportedLoader();
	}

	public void testLoadDefinition() throws Exception {
		assertThrows(UnsupportedOperationException.class, () -> loader.loadDefinition("def"));
	}

	public void testLoadGroup() throws Exception {
		assertThrows(UnsupportedOperationException.class, () -> loader.loadGroup("group"));
	}

}
