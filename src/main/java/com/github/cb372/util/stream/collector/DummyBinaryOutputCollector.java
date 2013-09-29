package com.github.cb372.util.stream.collector;

import java.util.concurrent.TimeUnit;

/**
 * Author: chris
 * Created: 9/29/13
 */
public class DummyBinaryOutputCollector implements BinaryOutputCollector {

    @Override
    public byte[] getBinaryOutput() throws InterruptedException {
        return new byte[0];
    }

    @Override
    public byte[] getBinaryOutput(long time, TimeUnit timeUnit) throws InterruptedException {
        return new byte[0];
    }
}
