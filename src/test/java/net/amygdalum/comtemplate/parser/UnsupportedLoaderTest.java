package net.amygdalum.comtemplate.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UnsupportedLoaderTest {

	private UnsupportedLoader loader;

	@BeforeEach
	public void before() throws Exception {
		loader = new UnsupportedLoader();
	}

	@Test
	public void testLoadDefinition() throws Exception {
		assertThrows(UnsupportedOperationException.class, () -> loader.loadDefinition("def"));
	}

	@Test
	public void testLoadGroup() throws Exception {
		assertThrows(UnsupportedOperationException.class, () -> loader.loadGroup("group"));
	}

}
