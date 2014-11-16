package com.github.cb372.util.stream;

import com.github.cb372.util.stream.listener.binary.ByteStreamListener;
import com.github.cb372.util.stream.listener.text.CharStreamListener;
import com.github.cb372.util.stream.listener.text.LoggingListener;
import com.github.cb372.util.stream.listener.text.PipingListener;
import com.github.cb372.util.stream.processor.ByteStreamProcessor;
import com.github.cb372.util.stream.processor.CharStreamProcessor;

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
public interface StreamProcessingThreadBuilder {

    public Thread build(InputStream stream);

    public static abstract class Base<T extends Base<T>> {
        private final Class<T> subclass;

        private String threadName = "CharStreamProcessor";
        private boolean daemon = Thread.currentThread().isDaemon();
        private int threadPriority = Thread.currentThread().getPriority();
        private IOExceptionHandler ioExceptionHandler = new ErrorLoggingIOExceptionHandler();
        private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        protected Base(Class<T> subclass) {
            this.subclass = subclass;
        }

        public T inDaemonThread() {
            this.daemon = true;
            return subclass.cast(this);
        }

        public T inNonDaemonThread() {
            this.daemon = false;
            return subclass.cast(this);
        }

        public T withThreadName(String threadName) {
            this.threadName = threadName;
            return subclass.cast(this);
        }

        public T withThreadPriority(int threadPriority) {
            this.threadPriority = threadPriority;
            return subclass.cast(this);
        }

        public T withIOExceptionHandler(IOExceptionHandler handler) {
            this.ioExceptionHandler = handler;
            return subclass.cast(this);
        }

        public T withUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
            this.uncaughtExceptionHandler = handler;
            return subclass.cast(this);
        }

        protected final Thread build(StreamProcessor streamProcessor) {
            StreamProcessingRunnable runnable = new StreamProcessingRunnable(streamProcessor, ioExceptionHandler);
            Thread thread = new Thread(runnable);
            thread.setName(threadName);
            thread.setDaemon(daemon);
            thread.setPriority(threadPriority);
            thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            return thread;
        }
    }

    public static final class ByteStreamProcessingThreadBuilder extends Base<ByteStreamProcessingThreadBuilder>
                                                                implements StreamProcessingThreadBuilder {
        private List<ByteStreamListener> listeners = new ArrayList<ByteStreamListener>();
        private int bufferSize = 1024;

        public ByteStreamProcessingThreadBuilder() {
            super(ByteStreamProcessingThreadBuilder.class);
        }

        public ByteStreamProcessingThreadBuilder withListener(ByteStreamListener listener) {
            this.listeners.add(listener);
            return this;
        }

        /**
         * Set the buffer size for reading binary data. Default is 1024 bytes.
         * @param bufferSize the desired buffer size in bytes (must be at least 1)
         */
        public ByteStreamProcessingThreadBuilder withBufferSize(int bufferSize) {
            if (bufferSize <= 0) {
                throw new IllegalArgumentException("Buffer size must be at least 1");
            }
            this.bufferSize = bufferSize;
            return this;
        }

        @Override
        public Thread build(InputStream stream) {
            StreamProcessor streamProcessor = new ByteStreamProcessor(stream, bufferSize, listeners);
            return build(streamProcessor);
        }
    }

    public static final class CharStreamProcessingThreadBuilder extends Base<CharStreamProcessingThreadBuilder>
                                                                implements StreamProcessingThreadBuilder {

        private final List<CharStreamListener> listeners = new ArrayList<CharStreamListener>();
        private Charset charset = Charset.forName("UTF-8");
        private String prefix = "";

        public CharStreamProcessingThreadBuilder() {
            super(CharStreamProcessingThreadBuilder.class);
        }

        public CharStreamProcessingThreadBuilder withCharset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public CharStreamProcessingThreadBuilder withCharset(String charset) {
            return withCharset(Charset.forName(charset));
        }

        public CharStreamProcessingThreadBuilder withPrefix(String prefix){
            this.prefix = prefix;
            return this;
        }

        public CharStreamProcessingThreadBuilder withListener(CharStreamListener listener) {
            listeners.add(listener);
            return this;
        }

        public CharStreamProcessingThreadBuilder withLogging(LoggingListener listener) {
            listeners.add(listener);
            return this;
        }

        public CharStreamProcessingThreadBuilder withLogging(LoggingListener.Builder listenerBuilder) {
            return withLogging(listenerBuilder.build());
        }

        public CharStreamProcessingThreadBuilder pipingToStdOut() {
            listeners.add(new PipingListener(System.out));
            return this;
        }

        public CharStreamProcessingThreadBuilder pipingToStdErr() {
            listeners.add(new PipingListener(System.err));
            return this;
        }

        @Override
        public Thread build(InputStream inputStream) {
            StreamProcessor streamProcessor = new CharStreamProcessor(inputStream, charset, prefix, listeners);
            return build(streamProcessor);
        }
    }

    public static final class ChooseStreamType
        extends Base<ChooseStreamType> {

        public ChooseStreamType() {
            super(ChooseStreamType.class);
        }

        public CharStreamProcessingThreadBuilder asText() {
            return new CharStreamProcessingThreadBuilder();
        }

        public ByteStreamProcessingThreadBuilder asBinary() {
            return new ByteStreamProcessingThreadBuilder();
        }
    }

}

