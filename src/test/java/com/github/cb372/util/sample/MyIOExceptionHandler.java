package com.github.cb372.util.sample;

import com.github.cb372.util.stream.IOExceptionHandler;

import java.io.IOException;

/**
* Author: chris
* Created: 4/5/13
*/
class MyIOExceptionHandler implements IOExceptionHandler {
    @Override
    public void handle(IOException e) {
        // Called if the stream processing thread throws an IOException
    }
}
