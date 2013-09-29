package com.github.cb372.util.stream.collector;

import java.util.concurrent.TimeUnit;

/**
 * Author: chris
 * Created: 9/29/13
 */
public interface BinaryOutputCollector {

    public byte[] getBinaryOutput() throws InterruptedException;

    public byte[] getBinaryOutput(long time, TimeUnit timeUnit) throws InterruptedException;

}
