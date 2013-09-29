package com.github.cb372.util.process;

import com.github.cb372.util.stream.collector.BinaryOutputCollector;
import com.github.cb372.util.stream.collector.DummyBinaryOutputCollector;
import com.github.cb372.util.stream.collector.DummyTextOutputCollector;
import com.github.cb372.util.stream.collector.TextOutputCollector;
import com.github.cb372.util.stream.StreamProcessingThreadBuilder;
import com.github.cb372.util.stream.listener.binary.BinaryOutputCollectingListener;
import com.github.cb372.util.stream.listener.text.TextOutputCollectingListener;

import java.io.File;
import java.io.IOException;

import static com.github.cb372.util.process.StreamProcessing.consume;
import static com.github.cb372.util.stream.StreamProcessingThreadBuilder.ByteStreamProcessingThreadBuilder;
import static com.github.cb372.util.stream.StreamProcessingThreadBuilder.CharStreamProcessingThreadBuilder;

/**
 * Author: chris
 * Created: 4/5/13
 */
public class ExternalProcessBuilder<T extends ExternalProcessBuilder<T>> {
    private final Class<T> subclass;

    protected final ProcessBuilder processBuilder;
    protected StreamProcessingThreadBuilder stderrProcessingThreadBuilder = new CharStreamProcessingThreadBuilder();
    protected boolean collectStdOut = false;

    protected ExternalProcessBuilder(Class<T> subclass, ProcessBuilder processBuilder) {
        this.subclass = subclass;
        this.processBuilder = processBuilder;
    }

    protected ExternalProcessBuilder(Class<T> subclass,
                                     ProcessBuilder processBuilder,
                                     StreamProcessingThreadBuilder stderrProcessingThreadBuilder,
                                     boolean collectStdOut) {
        this(subclass, processBuilder);
        this.stderrProcessingThreadBuilder = stderrProcessingThreadBuilder;
        this.collectStdOut = collectStdOut;
    }

    /**
     * Set the working directory for the process
     * @param dir working directory
     * @return builder
     */
    public T withWorkingDirectory(File dir) {
        processBuilder.directory(dir);
        return subclass.cast(this);
    }

    /**
     * Delete all environment variables from the process's environment.
     * By default the process will inherit the parent's environment.
     * @return builder
     */
    public T clearEnvironment() {
        processBuilder.environment().clear();
        return subclass.cast(this);
    }

    /**
     * Set an environment variable in the process's environment.
     * If it is already set, it will be overwritten.
     * @param key key
     * @param value value
     * @return builder
     */
    public T withEnvVar(String key, String value) {
        processBuilder.environment().put(key, value);
        return subclass.cast(this);
    }

    /**
     * Redirect the process's stderr to stdout.
     * Note: If this is called, any calls to {@link #processStdErr(com.github.cb372.util.stream.StreamProcessingThreadBuilder) processStdErr} will be ignored.
     *
     * @return builder
     */
    public T redirectingErrorStream() {
        processBuilder.redirectErrorStream(true);
        return subclass.cast(this);
    }

    /**
     * Set options for how to handle the process's stderr.
     * By default the stream will be silently consumed and discarded.
     * @param streamProcessingThreadBuilder
     * @return builder
     */
    public T processStdErr(StreamProcessingThreadBuilder streamProcessingThreadBuilder) {
        this.stderrProcessingThreadBuilder = streamProcessingThreadBuilder;
        return subclass.cast(this);
    }

    /**
     * <p>
     * Collect all data that the process sends to stdout, as a list of Strings.
     * </p>
     * <p>
     * If you set this option, you can access this data by calling
     * {@link TextCollectingExternalProcess#getTextOutput()} or
     * {@link BinaryOutputCollectingExternalProcess#getBinaryOutput()}
     * after the process has completed.
     * </p>
     * <p>
     * Warning: The amount of data that can be collected is unbounded,
     * so you may encounter OutOfMemory problems if your process has a lot of output.
     * </p>
     * <p>
     * For this reason, this option is disabled by default.
     * </p>
     *
     * @return builder
     */
    public T collectStdOut() {
        this.collectStdOut = true;
        return subclass.cast(this);
    }

    public static final class UnspecifiedStdOut extends ExternalProcessBuilder<UnspecifiedStdOut> {
        public UnspecifiedStdOut(ProcessBuilder processBuilder) {
            super(UnspecifiedStdOut.class, processBuilder);
        }

        /**
         * Set options for how to handle the process's stdout.
         * By default the stream will be silently consumed and discarded.
         * @param charStreamProcessingThreadBuilder
         * @return builder
         */
        public TextStdOut processStdOut(CharStreamProcessingThreadBuilder charStreamProcessingThreadBuilder) {
            return new TextStdOut(
                    processBuilder, stderrProcessingThreadBuilder, collectStdOut, charStreamProcessingThreadBuilder);
        }

        /**
         * Set options for how to handle the process's stdout.
         * By default the stream will be silently consumed and discarded.
         * @param byteStreamProcessingThreadBuilder
         * @return builder
         */
        public BinaryStdOut processStdOut(ByteStreamProcessingThreadBuilder byteStreamProcessingThreadBuilder) {
            return new BinaryStdOut(
                    processBuilder, stderrProcessingThreadBuilder, collectStdOut, byteStreamProcessingThreadBuilder);
        }

        public TextCollectingExternalProcess start() throws IOException {
            return processStdOut(consume().asText()).start();
        }
    }

    public static final class TextStdOut extends ExternalProcessBuilder<TextStdOut> {
        private CharStreamProcessingThreadBuilder stdoutProcessingThreadBuilder;

        protected TextStdOut(ProcessBuilder processBuilder,
                               StreamProcessingThreadBuilder stderrProcessingThreadBuilder,
                               boolean collectStdOut,
                               CharStreamProcessingThreadBuilder stdoutProcessingThreadBuilder) {
            super(TextStdOut.class, processBuilder, stderrProcessingThreadBuilder, collectStdOut);
            this.stdoutProcessingThreadBuilder = stdoutProcessingThreadBuilder;
        }

        /**
         * Start the process.
         * @return the started process
         * @throws IOException if the process failed to start
         */
        public TextCollectingExternalProcess start() throws IOException {
            // Start the process
            Process process = processBuilder.start();

            TextOutputCollector outputCollector;
            if (collectStdOut) {
                TextOutputCollectingListener outputCollectingListener = new TextOutputCollectingListener();
                stdoutProcessingThreadBuilder.withListener(outputCollectingListener);
                outputCollector = outputCollectingListener;
            } else {
                outputCollector = new DummyTextOutputCollector();
            }

            // Start the output stream processing threads
            Thread stdoutGobblerThread = stdoutProcessingThreadBuilder.build(process.getInputStream());
            stdoutGobblerThread.start();
            if (!processBuilder.redirectErrorStream()) {
                Thread stderrGobblerThread = stderrProcessingThreadBuilder.build(process.getErrorStream());
                stderrGobblerThread.start();
            }

            // return the process
            return new TextCollectingJavaLangProcessWrapper(process, outputCollector);
        }

    }

    public static final class BinaryStdOut extends ExternalProcessBuilder<BinaryStdOut> {
        private ByteStreamProcessingThreadBuilder stdoutProcessingThreadBuilder;

        protected BinaryStdOut(ProcessBuilder processBuilder,
                               StreamProcessingThreadBuilder stderrProcessingThreadBuilder,
                               boolean collectStdOut,
                               ByteStreamProcessingThreadBuilder stdoutProcessingThreadBuilder) {
            super(BinaryStdOut.class, processBuilder, stderrProcessingThreadBuilder, collectStdOut);
            this.stdoutProcessingThreadBuilder = stdoutProcessingThreadBuilder;
        }

        /**
         * Start the process.
         * @return the started process
         * @throws IOException if the process failed to start
         */
        public BinaryOutputCollectingExternalProcess start() throws IOException {
            // Start the process
            Process process = processBuilder.start();

            BinaryOutputCollector outputCollector;
            if (collectStdOut) {
                BinaryOutputCollectingListener outputCollectingListener = new BinaryOutputCollectingListener();
                stdoutProcessingThreadBuilder.withListener(outputCollectingListener);
                outputCollector = outputCollectingListener;
            } else {
                outputCollector = new DummyBinaryOutputCollector();
            }

            // Start the output stream processing threads
            Thread stdoutGobblerThread = stdoutProcessingThreadBuilder.build(process.getInputStream());
            stdoutGobblerThread.start();
            if (!processBuilder.redirectErrorStream()) {
                Thread stderrGobblerThread = stderrProcessingThreadBuilder.build(process.getErrorStream());
                stderrGobblerThread.start();
            }

            // return the process
            return new BinaryOutputCollectingJavaLangProcessWrapper(process, outputCollector);
        }

    }

}


