package com.github.cb372.util.stream.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A listener that collects all output from a stream.
 *
 * Note that the collection may not complete until slightly after the process exits.
 *
 * Warning: The amount of data that can be collected is unbounded,
 * so you may encounter OutOfMemory problems if your process has a lot of output.
 *
 * Author: chris
 * Created: 4/5/13
 */
public class OutputCollector implements StreamListener {
    private List<String> lines = new ArrayList<String>();
    private CountDownLatch complete = new CountDownLatch(1);

    @Override
    public void onChar(char c) {
        // do nothing
    }

    @Override
    public void onLine(String line) {
        lines.add(line);
    }

    @Override
    public void onEndOfStream() {
        complete.countDown();
    }

    /**
     * Wait until all output has been collected
     * @throws InterruptedException
     */
    public void awaitCompletion() throws InterruptedException {
        complete.await();
    }

    /**
     * Wait until all output has been completed, or until timeout occurs.
     * @param time timeout
     * @param timeUnit timeout units
     * @return false if timed out
     * @throws InterruptedException
     */
    public boolean awaitCompletion(long time, TimeUnit timeUnit) throws InterruptedException {
        return complete.await(time, timeUnit);
    }

    /**
     * Get the collected output. Warning: will block if not all ouput has yet been collected.
     * @return lines of output
     * @throws InterruptedException
     */
    public List<String> get() throws InterruptedException {
        awaitCompletion();
        return Collections.unmodifiableList(lines);
    }

    /**
     * Get the collected output. Warning: will block if not all ouput has yet been collected.
     * @param time timeout
     * @param timeUnit timeout unit
     * @return lines of output
     * @throws InterruptedException
     */
    public List<String> get(long time, TimeUnit timeUnit) throws InterruptedException {
        awaitCompletion(time, timeUnit);
        return Collections.unmodifiableList(lines);
    }
}
