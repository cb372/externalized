package com.github.cb372.util.stream.processor;

import com.github.cb372.util.stream.listener.binary.ByteStreamListener;
import com.github.cb372.util.stream.listener.text.CharStreamListener;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Author: chris
 * Created: 4/5/13
 */
public class ByteStreamProcessorTest {

    static final Charset utf8 = Charset.forName("UTF-8");

    @Test
    public void handlesEmptyStream() throws IOException {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        MockListener listener = new MockListener();
        new ByteStreamProcessor(stream, 1024, listener).run();

        assertThat(listener.calls.size(), is(1));
        assertThat(listener.calls.get(0), is("EOS"));
    }

    @Test
    public void gobblesStreamAndCallsListenerCallbacks() throws IOException {
        String text = "abcdef";
        InputStream stream = new ByteArrayInputStream(text.getBytes(utf8));
        MockListener listener = new MockListener();
        new ByteStreamProcessor(stream, 1024, listener).run();

        assertThat(listener.calls.get(0), is("abcdef"));
        assertThat(listener.calls.get(1), is("EOS"));
    }

    static class MockListener implements ByteStreamListener {
        private List<String> calls = new ArrayList<String>();

        @Override
        public void onBytes(byte[] b, int offset, int len) {
            assertThat(b.length, is(1024));
            byte[] bytes = new byte[len];
            System.arraycopy(b, offset, bytes, 0, len);
            calls.add(new String(bytes, utf8));
        }

        @Override
        public void onEndOfStream() {
            calls.add("EOS");
        }
    }
}
