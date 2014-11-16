package com.github.cb372.util.stream.processor;

import com.github.cb372.util.stream.listener.text.CharStreamListener;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Author: chris
 * Created: 4/5/13
 */
public class CharStreamProcessorTest {

    Charset utf8 = Charset.forName("UTF-8");

    @Test
    public void handlesEmptyStream() throws IOException {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        MockListener listener = new MockListener();
        new CharStreamProcessor(stream, utf8, listener).run();

        assertThat(listener.onCharCalls, is(list("EOS")));
        assertThat(listener.onLineCalls, is(list("EOS")));
    }

    @Test
    public void gobblesStreamAndCallsListenerCallbacks() throws IOException {
        String text = "abc\ndef";
        InputStream stream = new ByteArrayInputStream(text.getBytes(utf8));
        MockListener listener = new MockListener();
        new CharStreamProcessor(stream, utf8, listener).run();

        assertThat(listener.onCharCalls, is(list(
                "a", "b", "c", "\n",
                "d", "e", "f",
                "EOS")));
        assertThat(listener.onLineCalls, is(list("abc", "def", "EOS")));
    }

    @Test
    public void handlesJapaneseCharsCorrectly() throws IOException {
        String text = "あいう\nかきく";
        InputStream stream = new ByteArrayInputStream(text.getBytes(utf8));
        MockListener listener = new MockListener();
        new CharStreamProcessor(stream, utf8, listener).run();

        assertThat(listener.onCharCalls, is(list(
                "あ", "い", "う", "\n",
                "か", "き", "く",
                "EOS")));
        assertThat(listener.onLineCalls, is(list("あいう", "かきく", "EOS")));
    }

    @Test
    public void handlesCRLFLineEndingsCorrectly() throws IOException {
        String text = "abc\r\ndef";
        InputStream stream = new ByteArrayInputStream(text.getBytes(utf8));
        MockListener listener = new MockListener();
        new CharStreamProcessor(stream, utf8, listener).run();

        assertThat(listener.onCharCalls, is(list(
                "a", "b", "c", "\r", "\n",
                "d", "e", "f",
                "EOS")));
        assertThat(listener.onLineCalls, is(list("abc", "def", "EOS")));
    }

    @Test
    public void noDanglingEmptyLinesWhenLastCharIsLF() throws IOException {
        String text = "abc\r\ndef\n";
        MockListener listener = new MockListener();
        new CharStreamProcessor(new ByteArrayInputStream(text.getBytes(utf8)), utf8, listener).run();

        assertThat(listener.onCharCalls, is(list(
                "a", "b", "c", "\r", "\n",
                "d", "e", "f", "\n",
                "EOS")));
        assertThat(listener.onLineCalls, is(list("abc", "def", "EOS")));
    }

    @Test
    public void noDanglingEmptyLinesWhenLastCharIsCR() throws IOException {
        String text = "abc\r\ndef\r";
        MockListener listener = new MockListener();
        new CharStreamProcessor(new ByteArrayInputStream(text.getBytes(utf8)), utf8, listener).run();

        assertThat(listener.onCharCalls, is(list(
                "a", "b", "c", "\r", "\n",
                "d", "e", "f", "\r",
                "EOS")));
        assertThat(listener.onLineCalls, is(list("abc", "def", "EOS")));
    }

    @Test
    public void noDanglingEmptyLinesWhenLastCharIsCRLF() throws IOException {
        String text = "abc\r\ndef\r\n";
        MockListener listener = new MockListener();
        new CharStreamProcessor(new ByteArrayInputStream(text.getBytes(utf8)), utf8, listener).run();

        assertThat(listener.onCharCalls, is(list(
                "a", "b", "c", "\r", "\n",
                "d", "e", "f", "\r", "\n",
                "EOS")));
        assertThat(listener.onLineCalls, is(list("abc", "def", "EOS")));
    }

    @Test
    public void handlesEmptyLinesCorrectly() throws IOException {
        String text = "a\n\n\nb\n\n";
        InputStream stream = new ByteArrayInputStream(text.getBytes(utf8));
        MockListener listener = new MockListener();
        new CharStreamProcessor(stream, utf8, "", listener).run();

        assertThat(listener.onCharCalls, is(list(
                "a", "\n",
                "\n",
                "\n",
                "b", "\n",
                "\n",
                "EOS")));
        assertThat(listener.onLineCalls, is(list("a", "", "", "b", "", "EOS")));
    }

    @Test
    public void noLinePrefixOnEmptyInput() throws IOException {
        MockListener listener = new MockListener();
        new CharStreamProcessor(new ByteArrayInputStream(new byte[0]), utf8, ">>", listener).run();

        assertThat(listener.onCharCalls, is(list("EOS")));
        assertThat(listener.onLineCalls, is(list("EOS")));
    }

    @Test
    public void linePrefixIsAppendedToEachLineIncludingEmptyLines() throws IOException {
        String text = "abc\n\ndef";
        MockListener listener = new MockListener();
        new CharStreamProcessor(new ByteArrayInputStream(text.getBytes(utf8)), utf8, ">>", listener).run();

        assertThat(listener.onCharCalls, is(list(
                ">", ">", "a", "b", "c", "\n",
                ">", ">", "\n",
                ">", ">", "d", "e", "f",
                "EOS"
        )));
        assertThat(listener.onLineCalls, is(list(">>abc", ">>", ">>def", "EOS")));
    }

    @Test
    public void noLinePrefixIsAppendedWhenThereIsDanglingLF() throws IOException {
        String text = "abc\n";
        MockListener listener = new MockListener();
        new CharStreamProcessor(new ByteArrayInputStream(text.getBytes(utf8)), utf8, ">>", listener).run();

        assertThat(listener.onCharCalls, is(list(
                ">", ">", "a", "b", "c", "\n",
                "EOS"
        )));
        assertThat(listener.onLineCalls, is(list(">>abc","EOS")));
    }

    private List<String> list(String... strings) {
        return Arrays.asList(strings);
    }

    static class MockListener implements CharStreamListener {
        private List<String> onCharCalls = new ArrayList<String>();
        private List<String> onLineCalls = new ArrayList<String>();

        @Override
        public void onChar(char c) {
            onCharCalls.add(String.valueOf(c));
        }

        @Override
        public void onLine(String line) {
            onLineCalls.add(line);
        }

        @Override
        public void onEndOfStream() {
            onCharCalls.add("EOS");
            onLineCalls.add("EOS");
        }
    }
}
