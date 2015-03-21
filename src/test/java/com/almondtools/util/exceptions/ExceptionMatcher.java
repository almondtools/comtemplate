package com.almondtools.util.exceptions;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ExceptionMatcher<T extends Throwable> extends TypeSafeDiagnosingMatcher<T> {

	private Class<T> clazz;
	private String message;
	private Throwable cause;

	public ExceptionMatcher(Class<T> clazz) {
		this.clazz = clazz;
	}

	public static <T extends Throwable> ExceptionMatcher<T> matchesException(Class<T> clazz) {
		return new ExceptionMatcher<T>(clazz);
	}

	public ExceptionMatcher<T> withMessage(String message) {
		this.message = message;
		return this;
	}

	public ExceptionMatcher<T> withCause(Throwable cause) {
		this.cause = cause;
		return this;
	}

	@Override
	public void describeTo(Description description) {
		if (message != null) {
			description.appendText("should have clazz").appendValue(clazz.getSimpleName());
		}
		if (message != null) {
			description.appendText("should have message ").appendValue(message);
		}
		if (cause != null) {
			description.appendText("should have cause ").appendValue(cause);
		}
	}

	@Override
	protected boolean matchesSafely(T item, Description mismatchDescription) {
		if (item == null) {
			mismatchDescription.appendText("is null");
			return false;
		}
		if (!clazz.isInstance(item)) {
			mismatchDescription.appendText("should be instance of ").appendValue(clazz.getSimpleName()).appendText(" was ").appendValue(item.getClass().getSimpleName());
			return false;
		}
		if (message != null && !message.equals(item.getMessage())) {
			mismatchDescription.appendText("should have message ").appendValue(message).appendText(" and found ").appendValue(item.getMessage());
			return false;
		}
		if (cause != null && !cause.equals(item.getCause())) {
			mismatchDescription.appendText("should have cause ").appendValue(cause).appendText(" and found ").appendValue(item.getCause());
			return false;
		}
		return true;
	}

}
