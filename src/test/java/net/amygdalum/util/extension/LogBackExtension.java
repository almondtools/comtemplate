package net.amygdalum.util.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class LogBackExtension implements AfterEachCallback, ParameterResolver {

	private LogBackLog log;

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		if (log != null) {
			log.close();
		}
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext.getParameter().getType() == LogBackLog.class;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		ForClass forClass = parameterContext.getParameter().getDeclaredAnnotation(ForClass.class);
		Class<?> clazz = forClass == null ? Object.class : forClass.value();
		log = LogBackLog.forClass(clazz);
		log.init();
		return log;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.PARAMETER})
	public @interface ForClass {

		Class<?> value();

	}
}
