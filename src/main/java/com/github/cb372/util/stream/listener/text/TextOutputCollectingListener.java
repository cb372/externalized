package com.github.cb372.util.stream.listener.text;

import com.github.cb372.util.stream.collector.TextOutputCollector;
import com.github.cb372.util.stream.listener.OutputCollectingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
public class TextOutputCollectingListener extends OutputCollectingListener
                                          implements CharStreamListener, TextOutputCollector {
    private List<String> lines = new ArrayList<String>();

    @Override
    public void onChar(char c) {
        // do nothing
    }

    @Override
    public void onLine(String line) {
        lines.add(line);
    }

    @Override
    public List<String> getTextOutput() throws InterruptedException {
        awaitCompletion();
        return Collections.unmodifiableList(lines);
    }

    @Override
    public List<String> getTextOutput(long time, TimeUnit timeUnit) throws InterruptedException {
        if (awaitOutputCollection(time, timeUnit)) {
            return Collections.unmodifiableList(lines);
        } else {
            return Collections.emptyList();
        }
    }
}
