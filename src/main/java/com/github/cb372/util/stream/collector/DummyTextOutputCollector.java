package com.github.cb372.util.stream.collector;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author: chris
 * Created: 4/6/13
 */
public class DummyTextOutputCollector implements TextOutputCollector {

    @Override
    public List<String> getTextOutput() throws InterruptedException {
        return Collections.emptyList();
    }

    @Override
    public List<String> getTextOutput(long time, TimeUnit timeUnit) throws InterruptedException {
        return Collections.emptyList();
    }

}
