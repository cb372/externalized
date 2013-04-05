package com.github.cb372.util.sample;

import com.github.cb372.util.stream.listener.StreamListener;

/**
* Author: chris
* Created: 4/5/13
*/
class MyCustomErrorListener implements StreamListener {

    @Override
    public void onChar(char c) {
        // Called once for every character of output
    }
    @Override
    public void onLine(String line) {
        // Called once for every line of output
    }
    @Override
    public void onEndOfStream() {
        // Called when the stream ends
    }
}
