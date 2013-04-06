package com.github.cb372.util.process;

import com.github.cb372.util.stream.collector.DummyOutputCollector;
import com.github.cb372.util.stream.collector.OutputCollector;
import com.github.cb372.util.stream.StreamProcessingThreadBuilder;
import com.github.cb372.util.stream.listener.OutputCollectingListener;

import java.io.File;
import java.io.IOException;

/**
 * Author: chris
 * Created: 4/5/13
 */
public final class ExternalProcessBuilder {
    private final ProcessBuilder processBuilder;
    private StreamProcessingThreadBuilder stdoutGobblerThreadBuilder = new StreamProcessingThreadBuilder();
    private StreamProcessingThreadBuilder stderrGobblerThreadBuilder = new StreamProcessingThreadBuilder();
    private boolean collectStdOut = false;

    protected ExternalProcessBuilder(ProcessBuilder processBuilder) {
        this.processBuilder = processBuilder;
    }

    /**
     * Set the working directory for the process
     * @param dir working directory
     * @return builder
     */
    public ExternalProcessBuilder withWorkingDirectory(File dir) {
        processBuilder.directory(dir);
        return this;
    }

    /**
     * Delete all environment variables from the process's environment.
     * By default the process will inherit the parent's environment.
     * @return builder
     */
    public ExternalProcessBuilder clearEnvironment() {
        processBuilder.environment().clear();
        return this;
    }

    /**
     * Set an environment variable in the process's environment.
     * If it is already set, it will be overwritten.
     * @param key key
     * @param value value
     * @return builder
     */
    public ExternalProcessBuilder withEnvVar(String key, String value) {
        processBuilder.environment().put(key, value);
        return this;
    }

    /**
     * Redirect the process's stderr to stdout.
     * Note: If this is called, any calls to {@link #processStdErr(com.github.cb372.util.stream.StreamProcessingThreadBuilder) processStdErr} will be ignored.
     *
     * @return builder
     */
    public ExternalProcessBuilder redirectingErrorStream() {
        processBuilder.redirectErrorStream(true);
        return this;
    }

    /**
     * Set options for how to handle the process's stdout.
     * By default the stream will be silently consumed and discarded.
     * @param streamProcessingThreadBuilder
     * @return builder
     */
    public ExternalProcessBuilder processStdOut(StreamProcessingThreadBuilder streamProcessingThreadBuilder) {
        this.stdoutGobblerThreadBuilder = streamProcessingThreadBuilder;
        return this;
    }

    /**
     * Set options for how to handle the process's stderr.
     * By default the stream will be silently consumed and discarded.
     * @param streamProcessingThreadBuilder
     * @return builder
     */
    public ExternalProcessBuilder processStdErr(StreamProcessingThreadBuilder streamProcessingThreadBuilder) {
        this.stderrGobblerThreadBuilder = streamProcessingThreadBuilder;
        return this;
    }

    /**
     * <p>
     * Collect all data that the process sends to stdout.
     * </p>
     * <p>
     * If you set this option, you can access this data by calling {@link com.github.cb372.util.process.ExternalProcess#getOutput()}
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
    public ExternalProcessBuilder collectStdOut() {
        this.collectStdOut = true;
        return this;
    }

    /**
     * Start the process.
     * @return the started process
     * @throws IOException if the process failed to start
     */
    public ExternalProcess start() throws IOException {
        // Start the process
        Process process = processBuilder.start();

        OutputCollector outputCollector;
        if (collectStdOut) {
            OutputCollectingListener outputCollectingListener = new OutputCollectingListener();
            stdoutGobblerThreadBuilder.withListener(outputCollectingListener);
            outputCollector = outputCollectingListener;
        } else {
            outputCollector = new DummyOutputCollector();
        }

        // Start the output stream processing threads
        Thread stdoutGobblerThread = stdoutGobblerThreadBuilder.build(process.getInputStream());
        stdoutGobblerThread.start();
        if (!processBuilder.redirectErrorStream()) {
            Thread stderrGobblerThread = stderrGobblerThreadBuilder.build(process.getErrorStream());
            stderrGobblerThread.start();
        }

        // return the process
        return new JavaLangProcessWrapper(process, outputCollector);
    }

}


