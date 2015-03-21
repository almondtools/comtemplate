package com.almondtools.util.objects;

import static java.lang.reflect.Modifier.isFinal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class UtilityClassMatcher extends TypeSafeMatcher<Class<?>> {

	public UtilityClassMatcher() {
	}

	public static UtilityClassMatcher isUtilityClass() {
		return new UtilityClassMatcher();
	}

	@Override
	public void describeTo(Description description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean matchesSafely(Class<?> item) {
		if (!isFinal(item.getModifiers())) {
			return false;
		}
		try {
			Constructor<?> constructor = item.getDeclaredConstructor();
			if (constructor.isAccessible()) {
				return false;
			}
			constructor.setAccessible(true);
			constructor.newInstance();
		} catch (InvocationTargetException e) {
			if (!(e.getCause() instanceof RuntimeException)) {
				return false;
			}
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			return false;
		}
		return true;
	}

}
