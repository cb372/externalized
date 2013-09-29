package com.github.cb372.util.stream.listener.text;

import java.io.PrintStream;

/**
 * A listener that copies every character to a PrintStream, e.g. STDOUT.
 *
 * Author: chris
 * Created: 4/5/13
 */
public final class PipingListener implements CharStreamListener {
    private final PrintStream out;

    public PipingListener(PrintStream out) {
        this.out = out;
    }

    @Override
    public void onChar(char c) {
        out.write((int) c);
    }

    @Override
    public void onLine(String line) {
        // do nothing
    }

    @Override
    public void onEndOfStream() {
        // do nothing
    }

}

