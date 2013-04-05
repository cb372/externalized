package com.github.cb372.util.stream.listener;

import com.github.cb372.util.stream.StreamListener;
import org.slf4j.Logger;

/**
 * A stream listener that logs every line of output.
 *
 * Author: chris
 * Created: 4/5/13
 */
public class LoggingListener implements StreamListener {
    public enum LogLevel {
        Trace, Debug, Info, Warn, Error
    }

    private final Logger logger;
    private final LogLevel level;
    private final String prefix;

    public LoggingListener(Logger logger, LogLevel level, String prefix) {
        this.logger = logger;
        this.level = level;
        this.prefix = prefix;
    }

    @Override
    public void onChar(char c) {
    }

    @Override
    public void onLine(String line) {
        String log = prefix + line;
        switch (level) {
            case Trace:
                logger.trace(log);
                break;
            case Debug:
                logger.debug(log);
                break;
            case Info:
                logger.info(log);
                break;
            case Warn:
                logger.warn(log);
                break;
            case Error:
                logger.error(log);
                break;
        }
    }

}
