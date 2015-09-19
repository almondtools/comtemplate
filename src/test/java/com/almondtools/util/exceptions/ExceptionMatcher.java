package com.almondtools.util.exceptions;

import static org.hamcrest.core.IsEqual.equalTo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ExceptionMatcher<T extends Throwable> extends TypeSafeDiagnosingMatcher<T> {

	private Class<T> clazz;
	private Matcher<String> message;
	private Matcher<Throwable> cause;

	public ExceptionMatcher(Class<T> clazz) {
		this.clazz = clazz;
	}

	public static <T extends Throwable> ExceptionMatcher<T> matchesException(Class<T> clazz) {
		return new ExceptionMatcher<T>(clazz);
	}

	public ExceptionMatcher<T> withMessage(String message) {
		this.message = equalTo(message);
		return this;
	}

	public ExceptionMatcher<T> withMessage(Matcher<String> message) {
		this.message = message;
		return this;
	}

	public ExceptionMatcher<T> withCause(Throwable cause) {
		this.cause = equalTo(cause);
		return this;
	}

	public ExceptionMatcher<T> withCause(Matcher<Throwable> cause) {
		this.cause = cause;
		return this;
	}

	@Override
	public void describeTo(Description description) {
		if (message != null) {
			description.appendText("\nwith class ").appendValue(clazz.getSimpleName());
		}
		if (message != null) {
			description.appendText("\nwith message ").appendValue(message);
		}
		if (cause != null) {
			description.appendText("\nwith cause ").appendValue(cause);
		}
	}

	@Override
	protected boolean matchesSafely(T item, Description mismatchDescription) {
		if (item == null) {
			mismatchDescription.appendText("is null");
			return false;
		}
		if (!clazz.isInstance(item)) {
			mismatchDescription.appendText("\ninstance of ").appendValue(clazz.getSimpleName()).appendText(" but was ").appendValue(item.getClass().getSimpleName());
			return false;
		}
		if (message != null && !message.matches(item.getMessage())) {
			mismatchDescription.appendText("\nwith message ").appendValue(message).appendText(" but found ").appendValue(item.getMessage());
			return false;
		}
		if (cause != null && !cause.matches(item.getCause())) {
			mismatchDescription.appendText("\nwith cause ").appendValue(cause).appendText(" but found ").appendValue(item.getCause());
			return false;
		}
		return true;
	}

}
