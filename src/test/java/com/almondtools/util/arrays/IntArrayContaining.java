package com.almondtools.util.arrays;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IntArrayContaining extends TypeSafeMatcher<int[]> {

	private int[] elements;

	public IntArrayContaining(int[] elements) {
		this.elements = elements;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("array containing ").appendValueList("", ", ", "", elements);
	}

	@Override
	protected boolean matchesSafely(int[] item) {
		if (item.length != elements.length) {
			return false;
		}
		for (int i = 0; i < item.length; i++) {
			if (item[i] != elements[i]) {
				return false;
			}
		}
		return true;
	}

}
