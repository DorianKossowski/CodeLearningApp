package com.server.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogTester extends ListAppender<ILoggingEvent> implements AutoCloseable {
    private final Logger logger;

    public LogTester(Class<?> clazz) {
        logger = (Logger) LoggerFactory.getLogger(clazz);
        logger.addAppender(this);
        start();
    }

    public void assertInfo(String msg) {
        assertMessage(msg, Level.INFO);
    }

    public void assertDebug(String msg) {
        assertMessage(msg, Level.DEBUG);
    }

    public void assertError(String msg) {
        assertMessage(msg, Level.ERROR);
    }

    private void assertMessage(String msg, Level level) {
        boolean contains = list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains(msg) && event.getLevel().equals(level));
        assertTrue(contains);
    }

    @Override
    public void close() {
        list.clear();
        logger.detachAndStopAllAppenders();
    }
}