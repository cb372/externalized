package com.github.cb372.util.process;

/**
* Author: chris
* Created: 4/5/13
*/
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        // Called if the stream processing thread throws a RuntimeException
    }
}
