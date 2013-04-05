package com.github.cb372.util.process;


import com.github.cb372.util.stream.StreamListener;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Author: chris
 * Created: 4/5/13
 */
public class ExternalProcessTest {

    @Test
    public void processReturnsCorrectExitCode() throws IOException, InterruptedException {
        ExternalProcess process = Command.parse("src/test/resources/myscript.sh").start();
        assertThat(process.waitFor(), equalTo(123));
    }

    @Test
    public void processOutputIsPassedToListeners() throws IOException, InterruptedException {
        final CountDownLatch linesRead = new CountDownLatch(3);
        StreamListener stdoutListener = mockListener(linesRead);
        StreamListener stderrListener = mockListener(linesRead);

        ExternalProcess process = Command.parse("src/test/resources/myscript.sh")
                .processStdOut(StreamProcessing.gobble().withListener(stdoutListener))
                .processStdErr(StreamProcessing.gobble().withListener(stderrListener))
                .start();
        process.waitFor();

        assertThat(linesRead.await(1, TimeUnit.SECONDS), is(true));
        verify(stdoutListener).onLine("hello");
        verify(stdoutListener).onLine("world");
        verify(stderrListener).onLine("oh noes");
    }

    @Test
    public void canRedirectStderrToStdout() throws IOException, InterruptedException {
        final CountDownLatch linesRead = new CountDownLatch(3);
        StreamListener stdoutListener = mockListener(linesRead);

        ExternalProcess process = Command.parse("src/test/resources/myscript.sh")
                .processStdOut(StreamProcessing.gobble().withListener(stdoutListener))
                .redirectingErrorStream()
                .start();
        process.waitFor();

        assertThat(linesRead.await(1, TimeUnit.SECONDS), is(true));
        verify(stdoutListener).onLine("hello");
        verify(stdoutListener).onLine("world");
        verify(stdoutListener).onLine("oh noes");
    }

    @Test
    public void canSetEnvironmentVariables() throws IOException, InterruptedException {
        final CountDownLatch linesRead = new CountDownLatch(1);
        StreamListener stdoutListener = mockListener(linesRead);

        ExternalProcess process = Command.parse("src/test/resources/echo-foo.sh")
                .withEnvVar("FOO", "bar")
                .processStdOut(StreamProcessing.gobble().withListener(stdoutListener))
                .start();
        process.waitFor();

        assertThat(linesRead.await(1, TimeUnit.SECONDS), is(true));
        verify(stdoutListener).onLine("bar");
    }

    private StreamListener mockListener(final CountDownLatch linesRead) {
        StreamListener listener = mock(StreamListener.class);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                linesRead.countDown();
                return null;
            }
        }).when(listener).onLine(anyString());

        return listener;
    }
}
