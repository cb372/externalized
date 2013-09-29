package com.github.cb372.util.stream.listener.binary;

import com.github.cb372.util.stream.collector.BinaryOutputCollector;
import com.github.cb372.util.stream.listener.OutputCollectingListener;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Author: chris
 * Created: 9/29/13
 */
public class BinaryOutputCollectingListener extends OutputCollectingListener
                                            implements ByteStreamListener, BinaryOutputCollector {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    @Override
    public void onBytes(byte[] b, int offset, int len) {
        baos.write(b, offset, len);
    }

    @Override
    public byte[] getBinaryOutput() throws InterruptedException {
        awaitCompletion();
        return baos.toByteArray();
    }

    @Override
    public byte[] getBinaryOutput(long time, TimeUnit timeUnit) throws InterruptedException {
        if (awaitOutputCollection(time, timeUnit)) {
            return baos.toByteArray();
        } else {
            return new byte[0];
        }
    }

}
