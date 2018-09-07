package net.amygdalum.util.stream;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Windowed<T> implements Spliterator<T[]> {

	private Spliterator<T> source;
	private T[] window;

	private Windowed(Stream<T> stream, T[] window) {
		this.source = stream.spliterator();
		this.window = window;
	}

	public static <T> Stream<T[]> windowed(Stream<T> stream, T[] window) {
		return StreamSupport.stream(new Windowed<>(stream, window), false);
	}

	@Override
	public boolean tryAdvance(Consumer<? super T[]> action) {
		int lasti = window.length - 1;
		for (int i = 0; i < lasti; i++) {
			window[i] = window[i+1];
		}

		boolean next = source.tryAdvance(element -> {
			window[lasti] = element;
			action.accept(window);
		});
		if (!next) {
			window[lasti] = null;
			next = window[0] != null;
		}
		return next;

	}

	@Override
	public Spliterator<T[]> trySplit() {
		return null;
	}

	@Override
	public long estimateSize() {
		long size = source.estimateSize();
		return size < window.length
			? 0
			: size - window.length + 1;
	}

	@Override
	public int characteristics() {
		return ORDERED | NONNULL | (source.characteristics() & SIZED);
	}

}
