package com.github.cb372.util.process;

import com.github.cb372.util.stream.StreamGobblerThreadBuilder;

/**
 * Author: chris
 * Created: 4/5/13
 */
public final class StreamProcessing {

    public static StreamGobblerThreadBuilder gobble() {
        return new StreamGobblerThreadBuilder();
    }

}
