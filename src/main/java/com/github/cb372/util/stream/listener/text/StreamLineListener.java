package com.github.cb372.util.stream.listener.text;

/**
 * A stream listener that ignores individual characters
 * and listens to output per-line.
 *
 * Author: chris
 * Created: 4/5/13
 */
public abstract class StreamLineListener implements CharStreamListener {

    @Override
    public final void onChar(char c) {
        // do nothing
    }

    @Override
    public void onEndOfStream() {
        // do nothing
    }
}
