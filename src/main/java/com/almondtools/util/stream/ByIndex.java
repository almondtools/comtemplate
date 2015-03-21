package com.almondtools.util.stream;

import java.util.function.Function;

public class ByIndex<T> {

	private int index;
	private int last;

	public ByIndex(int size) {
		this.index = 0;
		this.last = size -1;
	}

	public static <S> Function<S, StreamItem<S>> byIndex(int last) {
		return new ByIndex<S>(last).byIndex();
	}

	public Function<T, StreamItem<T>> byIndex() {
		return new Function<T, StreamItem<T>>() {
			@Override
			public StreamItem<T> apply(T t) {
				StreamItem<T> item = new StreamItem<T>(index, t, last);
				index++;
				return item;
			}
		};
	}

	public static class StreamItem<S> {
		public boolean last;
		public int index;
		public boolean first;
		public S value;

		private StreamItem(int index, S value, int last) {
			this.index = index;
			this.value = value;
			this.first = index == 0;
			this.last = index == last;
		}

	}
}