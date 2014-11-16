package com.github.cb372.util.stream.processor;

import com.github.cb372.util.stream.StreamProcessor;
import com.github.cb372.util.stream.listener.text.CharStreamListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * A CharStreamProcessor consumes an input stream, notifying listeners on
 * every character and every line read.
 *
 * It is essential to consume the outpt and error streams of a process,
 * as some processes hang when their output buffers fill up.
 *
 * Author: chris
 * Created: 4/5/13
 */
public final class CharStreamProcessor implements StreamProcessor {
    private final InputStream stream;
    private final Charset charset;
    private final String linePrefix;
    private final List<CharStreamListener> listeners;

    public CharStreamProcessor(InputStream stream, Charset charset, String linePrefix, List<CharStreamListener> listeners) {
        this.stream = stream;
        this.charset = charset;
        this.linePrefix = linePrefix;
        this.listeners = listeners;
    }

    public CharStreamProcessor(InputStream stream, Charset charset, List<CharStreamListener> listeners) {
        this(stream, charset, "", listeners);
    }

    public CharStreamProcessor(InputStream stream, Charset charset, CharStreamListener... listeners) {
        this(stream, charset, Arrays.asList(listeners));
    }

    public CharStreamProcessor(InputStream stream, Charset charset, String linePrefix, CharStreamListener... listeners) {
        this(stream, charset, linePrefix, Arrays.asList(listeners));
    }

    @Override
    public void run() throws IOException {
        int c;
        StringBuilder line = new StringBuilder();
        boolean lastWasCR = false;
        char[] prefixChars = linePrefix.toCharArray();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
        try {
            while ((c = reader.read()) != -1) {
                char ch = (char) c;
                if (line.length() == 0) {
                    outputPrefix(prefixChars, line);
                }
                switch (ch) {
                    case '\r':
                        onLine(line);
                        break;
                    case '\n':
                        if (!lastWasCR) {
                            onLine(line);
                        }
                        break;
                    default:
                        line.append(ch);
                }
                onChar(ch);
                lastWasCR = ch == '\r';
            }
            // drain the last line
            if (line.length() > 0) {
                onLine(line);
            }
            // tell listeners that it is EOS
            onEOS();
        } finally {
            reader.close();
        }
    }

    private void outputPrefix(char[] prefixChars, StringBuilder line) {
        for (char prefixChar : prefixChars) {
            onChar(prefixChar);
            line.append(prefixChar);
        }
    }

    private void onLine(StringBuilder b) {
        final String line = b.toString();
        for (CharStreamListener listener : listeners) {
            listener.onLine(line);
        }
        b.setLength(0);
    }

    private void onChar(char c) {
        for (CharStreamListener listener : listeners) {
            listener.onChar(c);
        }
    }

    private void onEOS() {
        for (CharStreamListener listener : listeners) {
            listener.onEndOfStream();
        }
    }
}
