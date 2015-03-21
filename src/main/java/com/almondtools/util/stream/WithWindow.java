package com.almondtools.util.stream;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Function;

public class WithWindow<T> {

	private T[] window;

	@SuppressWarnings("unchecked")
	public WithWindow(int size, Class<T> clazz) {
		this.window = (T[]) Array.newInstance(clazz, size);
	}

	public static <S> Function<S, WindowItem<S>> withWindow(int size, Class<S> clazz) {
		return new WithWindow<S>(size, clazz).withWindow();
	}

	public Function<T, WindowItem<T>> withWindow() {
		return new Function<T, WindowItem<T>>() {
			@Override
			public WindowItem<T> apply(T t) {
				Arrays.setAll(window, e -> {
					if (e >= window.length - 1) {
						return t;
					} else {
						return window[e + 1];
					}
				});
				return new WindowItem<T>(window);
			}
		};
	}

	public static class WindowItem<S> {

		public S[] window;

		private WindowItem(S[] window) {
			this.window = window;
		}

	}
}