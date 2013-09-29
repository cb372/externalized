package com.github.cb372.util.process;

import java.io.IOException;

/**
 * Author: chris
 * Created: 9/29/13
 */
public class RoundtripBinaryData {
    public static void main(String[] args) throws IOException {
        byte[] buffer = new byte[1024];
        int byteCount;
        while ((byteCount = System.in.read(buffer)) > -1) {
            System.out.write(buffer, 0, byteCount);
        }
    }
}
