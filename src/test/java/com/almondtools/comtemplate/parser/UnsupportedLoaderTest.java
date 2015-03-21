package com.almondtools.comtemplate.parser;

import org.junit.Before;
import org.junit.Test;

public class UnsupportedLoaderTest {

	private UnsupportedLoader loader;

	@Before
	public void before() throws Exception {
		loader = new UnsupportedLoader();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testLoadDefinition() throws Exception {
		loader.loadDefinition("def");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testLoadGroup() throws Exception {
		loader.loadGroup("group");
	}

}
