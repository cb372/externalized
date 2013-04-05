package com.github.cb372.util.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Author: chris
 * Created: 4/5/13
 */
public final class StreamGobblerRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(StreamGobblerRunnable.class);

    private final StreamGobbler streamGobbler;
    private final IOExceptionHandler exceptionHandler;

    public StreamGobblerRunnable(StreamGobbler streamGobbler, IOExceptionHandler exceptionHandler) {
        this.streamGobbler = streamGobbler;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void run() {
        try {
            streamGobbler.gobble();
        } catch (IOException e) {
            exceptionHandler.handle(e);
        }
    }
}
