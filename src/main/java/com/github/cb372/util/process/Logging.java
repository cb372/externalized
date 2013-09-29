package com.github.cb372.util.process;

import com.github.cb372.util.stream.listener.text.LoggingListener;
import org.slf4j.Logger;

/**
 * Author: chris
 * Created: 4/5/13
 */
public final class Logging {

    public static LoggingListener.Builder usingLogger(Logger logger) {
        return new LoggingListener.Builder(logger);
    }

}
