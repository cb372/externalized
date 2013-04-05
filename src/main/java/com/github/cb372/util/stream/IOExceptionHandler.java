package com.github.cb372.util.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Author: chris
 * Created: 4/5/13
 */
public interface IOExceptionHandler {

    public void handle(IOException e);

}

class ErrorLoggingIOExceptionHandler implements IOExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorLoggingIOExceptionHandler.class);

    @Override
    public void handle(IOException e) {
        logger.warn("StreamProcessor threw an exception", e);
    }
}
