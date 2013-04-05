package com.github.cb372.util.stream;

import com.github.cb372.util.stream.listener.LoggingListener;
import com.github.cb372.util.stream.listener.PipingListener;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for a StreamGobbler thread.
 *
 * Author: chris
 * Created: 4/5/13
 */
public final class StreamGobblerThreadBuilder {
    private final List<StreamListener> listeners = new ArrayList<StreamListener>();
    private Charset charset = Charset.forName("UTF-8");

    private String threadName = "StreamGobbler";
    private boolean daemon = Thread.currentThread().isDaemon();
    private int threadPriority = Thread.currentThread().getPriority();
    private IOExceptionHandler ioExceptionHandler = new ErrorLoggingIOExceptionHandler();
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();


    public StreamGobblerThreadBuilder withCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public StreamGobblerThreadBuilder withCharset(String charset) {
        return withCharset(Charset.forName(charset));
    }

    public StreamGobblerThreadBuilder withListener(StreamListener listener) {
        listeners.add(listener);
        return this;
    }

    public StreamGobblerThreadBuilder withLogging(LoggingListener listener) {
        listeners.add(listener);
        return this;
    }

    public StreamGobblerThreadBuilder withLogging(LoggingListener.Builder listenerBuilder) {
        return withLogging(listenerBuilder.build());
    }

    public StreamGobblerThreadBuilder pipingToStdOut() {
        listeners.add(new PipingListener(System.out));
        return this;
    }

    public StreamGobblerThreadBuilder pipingToStdErr() {
        listeners.add(new PipingListener(System.err));
        return this;
    }

    public StreamGobblerThreadBuilder inDaemonThread() {
        this.daemon = true;
        return this;
    }

    public StreamGobblerThreadBuilder inNonDaemonThread() {
        this.daemon = false;
        return this;
    }

    public StreamGobblerThreadBuilder withThreadName(String threadName) {
        this.threadName = threadName;
        return this;
    }

    public StreamGobblerThreadBuilder withThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
        return this;
    }

    public StreamGobblerThreadBuilder withIOExceptionHandler(IOExceptionHandler handler) {
        this.ioExceptionHandler = handler;
        return this;
    }

    public StreamGobblerThreadBuilder withUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        this.uncaughtExceptionHandler = handler;
        return this;
    }

    public Thread build(InputStream stream) {
        StreamGobbler streamGobbler = new StreamGobbler(stream, charset, listeners);
        StreamGobblerRunnable runnable = new StreamGobblerRunnable(streamGobbler, ioExceptionHandler);
        Thread thread = new Thread(runnable);
        thread.setName(threadName);
        thread.setDaemon(daemon);
        thread.setPriority(threadPriority);
        thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        return thread;
    }
}
