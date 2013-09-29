package com.github.cb372.util.stream.listener;

/**
 * A listener with callback events for every character
 * and every line of output.
 *
 * Author: chris
 * Created: 4/5/13
 */
public interface StreamListener {

    /**
     * Called when the output has finished.
     */
    void onEndOfStream();

}
