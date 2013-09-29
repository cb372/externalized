package com.github.cb372.util.process;

import com.github.cb372.util.stream.collector.BinaryOutputCollector;

import java.util.concurrent.TimeUnit;

/**
 * Author: chris
 * Created: 9/29/13
 */
public interface BinaryOutputCollectingExternalProcess extends ExternalProcess, BinaryOutputCollector {
}

class BinaryOutputCollectingJavaLangProcessWrapper extends JavaLangProcessWrapper
                                                   implements BinaryOutputCollectingExternalProcess {
    private BinaryOutputCollector outputCollector;

    protected BinaryOutputCollectingJavaLangProcessWrapper(Process process, BinaryOutputCollector outputCollector) {
        super(process);
        this.outputCollector = outputCollector;
    }

    @Override
    public byte[] getBinaryOutput() throws InterruptedException {
        return outputCollector.getBinaryOutput();
    }

    @Override
    public byte[] getBinaryOutput(long time, TimeUnit timeUnit) throws InterruptedException {
        return outputCollector.getBinaryOutput(time, timeUnit);
    }
}
