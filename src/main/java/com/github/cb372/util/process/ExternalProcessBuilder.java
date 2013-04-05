package com.github.cb372.util.process;

import com.github.cb372.util.stream.StreamGobblerThreadBuilder;

import java.io.IOException;

/**
 * Author: chris
 * Created: 4/5/13
 */
public final class ExternalProcessBuilder {
    private final ProcessBuilder processBuilder;

    public ExternalProcessBuilder(ProcessBuilder processBuilder) {
        this.processBuilder = processBuilder;
    }

    public static ExternalProcessBuilder fromProcessBuilder(ProcessBuilder processBuilder) {
        return new ExternalProcessBuilder(processBuilder);
    }

    private StreamGobblerThreadBuilder stdoutGobblerThreadBuilder = new StreamGobblerThreadBuilder();
    private StreamGobblerThreadBuilder stderrGobblerThreadBuilder = new StreamGobblerThreadBuilder();

    public ExternalProcessBuilder processStdOut(StreamGobblerThreadBuilder streamGobblerThreadBuilder) {
        this.stdoutGobblerThreadBuilder = streamGobblerThreadBuilder;
        return this;
    }

    public ExternalProcessBuilder processStdErr(StreamGobblerThreadBuilder streamGobblerThreadBuilder) {
        this.stderrGobblerThreadBuilder = streamGobblerThreadBuilder;
        return this;
    }

    public ExternalProcess start() throws IOException {
        // Start the process
        Process process = processBuilder.start();

        // Start the output stream processing threads
        Thread stdoutGobblerThread = stdoutGobblerThreadBuilder.build(process.getInputStream());
        Thread stderrGobblerThread = stderrGobblerThreadBuilder.build(process.getErrorStream());
        stdoutGobblerThread.start();
        stderrGobblerThread.start();

        // return the process
        return new JavaLangProcessWrapper(process);
    }

}


