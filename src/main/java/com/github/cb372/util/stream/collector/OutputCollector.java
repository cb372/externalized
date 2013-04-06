package com.github.cb372.util.stream.collector;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author: chris
 * Created: 4/6/13
 */
public interface OutputCollector {

    /**
     * Get all data sent to the stream, as a list of strings.
     *
     * Note: This method will block until the stream is exhausted.
     *
     * @return lines of output
     * @throws InterruptedException
     */
    public List<String> getOutput() throws InterruptedException;

    /**
     *
     * Get all data sent to the stream, as a list of strings.
     *
     * Note: This method will block until either the stream is exhausted
     * or a timeout occurs. In the case of a timeout, an empty list will be returned.
     *
     * @param time timeout
     * @param timeUnit units of timeout
     * @return lines of output
     * @throws InterruptedException
     */
    public List<String> getOutput(long time, TimeUnit timeUnit) throws InterruptedException;

}
