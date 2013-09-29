package com.github.cb372.util.stream.listener.text;

import com.github.cb372.util.stream.listener.StreamListener;

/**
 * Author: chris
 * Created: 9/29/13
 */
public interface CharStreamListener extends StreamListener {

    /**
     * Called once for every character of output, including newline characters.
     * @param c character
     */
    public void onChar(char c);

    /**
     * Called once for every line of output.
     * @param line line (does not include newline characters)
     */
    public void onLine(String line);

}
