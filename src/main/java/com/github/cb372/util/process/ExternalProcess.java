package com.github.cb372.util.process;

import java.io.OutputStream;

/**
 * Similar to java.lang.Process, but does not expose the process's output streams.
 *
 * Author: chris
 * Created: 4/5/13
 */
public interface ExternalProcess {

    public OutputStream getStdIn();

    public int waitFor() throws InterruptedException;

    public int exitValue();

    public void destroy();

}

class JavaLangProcessWrapper implements ExternalProcess {
    private final Process process;

    JavaLangProcessWrapper(Process process) {
        this.process = process;
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
}