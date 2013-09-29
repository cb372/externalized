package com.github.cb372.util.stream.listener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Author: chris
 * Created: 9/29/13
 */
public class OutputCollectingListener implements StreamListener {
    private CountDownLatch complete = new CountDownLatch(1);

    public void onEndOfStream() {
        complete.countDown();
    }

    /**
     * Wait until all output has been collected.
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
    public boolean awaitOutputCollection(long time, TimeUnit timeUnit) throws InterruptedException {
        return complete.await(time, timeUnit);
    }
}
