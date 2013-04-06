package com.github.cb372.util.stream.collector;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author: chris
 * Created: 4/6/13
 */
public class DummyOutputCollector implements OutputCollector {

    @Override
    public List<String> getOutput() throws InterruptedException {
        return Collections.emptyList();
    }

    @Override
    public List<String> getOutput(long time, TimeUnit timeUnit) throws InterruptedException {
        return Collections.emptyList();
    }

}
