package com.github.cb372.util.process;

import com.github.cb372.util.stream.collector.OutputCollector;

import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Similar to java.lang.Process, but does not expose the process's output streams.
 *
 * Author: chris
 * Created: 4/5/13
 */
public interface ExternalProcess extends OutputCollector {

    public OutputStream getStdIn();

    public int waitFor() throws InterruptedException;

    public int exitValue();

    public void destroy();

}

class JavaLangProcessWrapper implements ExternalProcess {
    private final Process process;
    private final OutputCollector outputCollector;

    JavaLangProcessWrapper(Process process, OutputCollector outputCollector) {
        this.process = process;
        this.outputCollector = outputCollector;
    }

    @Override
    public OutputStream getStdIn() {
        return process.getOutputStream();
    }

    @Override
    public int waitFor() throws InterruptedException {
        return process.waitFor();
    }

    @Override
    public int exitValue() {
        return process.exitValue();
    }

    @Override
    public void destroy() {
        process.destroy();
    }

    /**
     * Get all data that was sent to the process's stdout.
     *
     * Note that this method will block until the process completes and its stdout has been exhausted.
     *
     * Will return an empty list unless you explicitly called {@link com.github.cb372.util.process.ExternalProcessBuilder#collectStdOut()}
     * when building the process.
     *
     * @return stdout output
     * @throws InterruptedException
     */
    @Override
    public List<String> getOutput() throws InterruptedException {
        return outputCollector.getOutput();
    }

    /**
     * Get all data that was sent to the process's stdout.
     *
     * Note that this method will block until the process completes and its stdout has been exhausted,
     * or until a timeout occurs.
     *
     * Will return an empty list unless you explicitly called {@link com.github.cb372.util.process.ExternalProcessBuilder#collectStdOut()}
     * when building the process.
     *
     * @return stdout output
     * @throws InterruptedException
     */
    @Override
    public List<String> getOutput(long time, TimeUnit timeUnit) throws InterruptedException {
        return outputCollector.getOutput(time, timeUnit);
    }

}