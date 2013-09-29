package com.github.cb372.util.process;

import com.github.cb372.util.stream.collector.TextOutputCollector;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface TextCollectingExternalProcess extends ExternalProcess, TextOutputCollector {}

class TextCollectingJavaLangProcessWrapper extends JavaLangProcessWrapper implements TextCollectingExternalProcess {
    private final TextOutputCollector textOutputCollector;

    protected TextCollectingJavaLangProcessWrapper(Process process, TextOutputCollector textOutputCollector) {
        super(process);
        this.textOutputCollector = textOutputCollector;
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
    public List<String> getTextOutput() throws InterruptedException {
        return textOutputCollector.getTextOutput();
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
    public List<String> getTextOutput(long time, TimeUnit timeUnit) throws InterruptedException {
        return textOutputCollector.getTextOutput(time, timeUnit);
    }

}
