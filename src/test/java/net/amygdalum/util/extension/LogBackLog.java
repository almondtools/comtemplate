package net.amygdalum.util.extension;

import static java.util.stream.Collectors.joining;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class LogBackLog {

	private Class<?> clazz;
	private ListAppender<ILoggingEvent> appender;

	public LogBackLog(Class<?> clazz) {
		this.clazz = clazz;
	}

	public void init() {
		appender = new ListAppender<ILoggingEvent>();
		appender.start();
		Logger logger = (Logger) LoggerFactory.getLogger(clazz);
		logger.addAppender(appender);
	}

	public void close() {
		Logger logger = (Logger) LoggerFactory.getLogger(clazz);
		logger.detachAppender(appender);
		appender.stop();
	}

	public String getMessages() {
		return appender.list.stream()
			.map(event -> event.getMessage())
			.collect(joining("\n"));
	}

	public String getError() {
		return appender.list.stream()
			.filter(event -> event.getLevel() == Level.ERROR)
			.map(event -> event.getMessage())
			.collect(joining("\n"));
	}

	public static LogBackLog forClass(Class<?> clazz) {
		return new LogBackLog(clazz);
	}

}
