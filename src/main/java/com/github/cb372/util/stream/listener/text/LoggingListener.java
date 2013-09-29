package com.github.cb372.util.stream.listener.text;

import org.slf4j.Logger;

/**
 * A stream listener that logs every line of output to a supplied Slf4j logger.
 *
 * Author: chris
 * Created: 4/5/13
 */
public final class LoggingListener extends StreamLineListener {
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

    public static class Builder {
        private final Logger logger;
        private LogLevel level = LogLevel.Info;
        private String prefix = "";

        public Builder(Logger logger) {
            this.logger = logger;
        }

        public Builder atLogLevel(LogLevel level) {
            this.level = level;
            return this;
        }

        public Builder withPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public LoggingListener build() {
            return new LoggingListener(logger, level, prefix);
        }

        public Builder atTraceLevel() {
            return atLogLevel(LogLevel.Trace);
        }

        public Builder atDebugLevel() {
            return atLogLevel(LogLevel.Debug);
        }

        public Builder atInfoLevel() {
            return atLogLevel(LogLevel.Info);
        }

        public Builder atWarnLevel() {
            return atLogLevel(LogLevel.Warn);
        }

        public Builder atErrorLevel() {
            return atLogLevel(LogLevel.Error);
        }
    }
}
