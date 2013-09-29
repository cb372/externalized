package com.github.cb372.util.stream;

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
            streamProcessor.run();
        } catch (IOException e) {
            exceptionHandler.handle(e);
        }
    }
}
