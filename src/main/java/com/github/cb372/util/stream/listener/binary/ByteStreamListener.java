package com.github.cb372.util.stream.listener.binary;

import com.github.cb372.util.stream.listener.StreamListener;

/**
 * Author: chris
 * Created: 9/29/13
 */
public interface ByteStreamListener extends StreamListener {

    public void onBytes(byte[] b, int offset, int len);

}
