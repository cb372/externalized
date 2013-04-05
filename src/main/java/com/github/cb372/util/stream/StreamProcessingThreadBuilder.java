package com.github.cb372.util.stream;

import com.github.cb372.util.stream.listener.LoggingListener;
import com.github.cb372.util.stream.listener.PipingListener;
import com.github.cb372.util.stream.listener.StreamListener;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for a StreamProcessor thread.
 *
 * Author: chris
 * Created: 4/5/13
 */
public final class StreamProcessingThreadBuilder {
    private final List<StreamListener> listeners = new ArrayList<StreamListener>();
    private Charset charset = Charset.forName("UTF-8");

    private String threadName = "StreamProcessor";
    private boolean daemon = Thread.currentThread().isDaemon();
    private int threadPriority = Thread.currentThread().getPriority();
    private IOExceptionHandler ioExceptionHandler = new ErrorLoggingIOExceptionHandler();
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();


    public StreamProcessingThreadBuilder withCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public StreamProcessingThreadBuilder withCharset(String charset) {
        return withCharset(Charset.forName(charset));
    }

    public StreamProcessingThreadBuilder withListener(StreamListener listener) {
        listeners.add(listener);
        return this;
    }

    public StreamProcessingThreadBuilder withLogging(LoggingListener listener) {
        listeners.add(listener);
        return this;
    }

    public StreamProcessingThreadBuilder withLogging(LoggingListener.Builder listenerBuilder) {
        return withLogging(listenerBuilder.build());
    }

    public StreamProcessingThreadBuilder pipingToStdOut() {
        listeners.add(new PipingListener(System.out));
        return this;
    }

    public StreamProcessingThreadBuilder pipingToStdErr() {
        listeners.add(new PipingListener(System.err));
        return this;
    }

    public StreamProcessingThreadBuilder inDaemonThread() {
        this.daemon = true;
        return this;
    }

    public StreamProcessingThreadBuilder inNonDaemonThread() {
        this.daemon = false;
        return this;
    }

    public StreamProcessingThreadBuilder withThreadName(String threadName) {
        this.threadName = threadName;
        return this;
    }

    public StreamProcessingThreadBuilder withThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
        return this;
    }

    public StreamProcessingThreadBuilder withIOExceptionHandler(IOExceptionHandler handler) {
        this.ioExceptionHandler = handler;
        return this;
    }

    public StreamProcessingThreadBuilder withUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        this.uncaughtExceptionHandler = handler;
        return this;
    }

    public Thread build(InputStream stream) {
        StreamProcessor streamProcessor = new StreamProcessor(stream, charset, listeners);
        StreamProcessingRunnable runnable = new StreamProcessingRunnable(streamProcessor, ioExceptionHandler);
        Thread thread = new Thread(runnable);
        thread.setName(threadName);
        thread.setDaemon(daemon);
        thread.setPriority(threadPriority);
        thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        return thread;
    }
}
