package com.github.cb372.util.stream;

/**
 * A listener with callback events for every character
 * and every line of output.
 *
 * Author: chris
 * Created: 4/5/13
 */
public interface StreamListener {

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
