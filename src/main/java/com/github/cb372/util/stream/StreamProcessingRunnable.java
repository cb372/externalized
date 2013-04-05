package com.github.cb372.util.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Author: chris
 * Created: 4/5/13
 */
public final class StreamProcessingRunnable implements Runnable {
    private final StreamProcessor streamProcessor;
    private final IOExceptionHandler exceptionHandler;

    public StreamProcessingRunnable(StreamProcessor streamProcessor, IOExceptionHandler exceptionHandler) {
        this.streamProcessor = streamProcessor;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void run() {
        try {
            streamProcessor.gobble();
        } catch (IOException e) {
            exceptionHandler.handle(e);
        }
    }
}
