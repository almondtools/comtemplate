package com.almondtools.util.logback;

import static java.util.stream.Collectors.joining;

import org.junit.rules.ExternalResource;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import com.almondtools.comtemplate.engine.DefaultErrorHandler;

public class LogBackLog extends ExternalResource {

	private Class<DefaultErrorHandler> clazz;
	private ListAppender<ILoggingEvent> appender;

	public LogBackLog(Class<DefaultErrorHandler> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected void before() {
		appender = new ListAppender<ILoggingEvent>();
		appender.start();
		Logger logger = (Logger) LoggerFactory.getLogger(clazz);
		logger.addAppender(appender);
	}

	@Override
	protected void after() {
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

	public static LogBackLog forClass(Class<DefaultErrorHandler> clazz) {
		return new LogBackLog(clazz);
	}

}
