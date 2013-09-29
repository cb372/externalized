package com.github.cb372.util.process;

import com.github.cb372.util.stream.StreamProcessingThreadBuilder;

/**
 * Author: chris
 * Created: 4/5/13
 */
public final class StreamProcessing {

    public static StreamProcessingThreadBuilder.ChooseStreamType consume() {
        return new StreamProcessingThreadBuilder.ChooseStreamType();
    }

}
