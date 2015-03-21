package com.almondtools.util.arrays;

import org.hamcrest.Matcher;

public class PrimitiveArrayMatchers {

	public static Matcher<int[]> arrayContaining(int... elements) {
		return new IntArrayContaining(elements);
	}

}
