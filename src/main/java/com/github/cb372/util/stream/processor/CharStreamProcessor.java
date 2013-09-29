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
    private final List<CharStreamListener> listeners;

    public CharStreamProcessor(InputStream stream, Charset charset, List<CharStreamListener> listeners) {
        this.stream = stream;
        this.charset = charset;
        this.listeners = listeners;
    }

    public CharStreamProcessor(InputStream stream, Charset charset, CharStreamListener... listeners) {
        this(stream, charset, Arrays.asList(listeners));
    }

    @Override
    public void run() throws IOException {
        int c;
        StringBuilder line = new StringBuilder();
        CharType lastSeen = CharType.Nil;

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
        try {
            while ((c = reader.read()) != -1) {
                char ch = (char) c;
                if (ch == '\r') {
                    switch (lastSeen) {
                        case CR:
                        case LF:
                            // two \r in a row = an empty line
                            // \n followed by \r = an empty line
                            onLine(line.toString());
                            line.setLength(0);
                            break;
                    }
                    lastSeen = CharType.CR;
                } else if (ch == '\n') {
                    switch (lastSeen) {
                        case LF:
                            // two \n in a row = an empty line
                            onLine(line.toString());
                            line.setLength(0);
                            break;
                    }
                    lastSeen = CharType.LF;
                } else {
                    switch (lastSeen) {
                        case CR:
                        case LF:
                            // start of new line
                            onLine(line.toString());
                            line.setLength(0);
                            break;
                    }
                    line.append(ch);
                    lastSeen = CharType.Normal;
                }
                // pass character to listener callbacks
                onChar(ch);
            }
            // drain the last line
            if (lastSeen != CharType.Nil) {
                onLine(line.toString());
            }
            // tell listeners that it is EOS
            onEOS();
        } finally {
            reader.close();
        }
    }

    private enum CharType { Nil, Normal, CR, LF }

    private void onLine(String line) {
        for (CharStreamListener listener : listeners) {
            listener.onLine(line);
        }
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
