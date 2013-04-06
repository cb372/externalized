package com.github.cb372.util.process;


import com.github.cb372.util.stream.listener.OutputCollectingListener;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        OutputCollectingListener stdoutListener = new OutputCollectingListener();
        OutputCollectingListener stderrListener = new OutputCollectingListener();

        ExternalProcess process = Command.parse("src/test/resources/myscript.sh")
                .processStdOut(StreamProcessing.consume().withListener(stdoutListener))
                .processStdErr(StreamProcessing.consume().withListener(stderrListener))
                .start();

        process.waitFor();
        assertThat(stdoutListener.awaitOutputCollection(1, TimeUnit.SECONDS), is(true));
        assertThat(stderrListener.awaitOutputCollection(1, TimeUnit.SECONDS), is(true));

        assertThat(stdoutListener.getOutput().get(0), equalTo("hello"));
        assertThat(stdoutListener.getOutput().get(1), equalTo("world"));
        assertThat(stderrListener.getOutput().get(0), equalTo("oh noes"));
    }

    @Test
    public void canRedirectStderrToStdout() throws IOException, InterruptedException {
        OutputCollectingListener stdoutListener = new OutputCollectingListener();

        ExternalProcess process = Command.parse("src/test/resources/myscript.sh")
                .processStdOut(StreamProcessing.consume().withListener(stdoutListener))
                .redirectingErrorStream()
                .start();

        process.waitFor();
        assertThat(stdoutListener.awaitOutputCollection(1, TimeUnit.SECONDS), is(true));

        assertThat(stdoutListener.getOutput().get(0), equalTo("hello"));
        assertThat(stdoutListener.getOutput().get(1), equalTo("world"));
        assertThat(stdoutListener.getOutput().get(2), equalTo("oh noes"));
    }

    @Test
    public void canSetEnvironmentVariables() throws IOException, InterruptedException {
        OutputCollectingListener stdoutListener = new OutputCollectingListener();

        ExternalProcess process = Command.parse("src/test/resources/echo-foo.sh")
                .withEnvVar("FOO", "bar")
                .processStdOut(StreamProcessing.consume().withListener(stdoutListener))
                .start();
        process.waitFor();

        assertThat(stdoutListener.awaitOutputCollection(1, TimeUnit.SECONDS), is(true));
        assertThat(stdoutListener.getOutput().get(0), equalTo("bar"));
    }

    @Test
    public void collectsProcessOutputIfToldTo() throws IOException, InterruptedException {
        ExternalProcess process = Command.parse("src/test/resources/myscript.sh")
                .collectStdOut()
                .start();
        process.waitFor();

        assertThat(process.getOutput().size(), equalTo(2));
        assertThat(process.getOutput().get(0), equalTo("hello"));
        assertThat(process.getOutput().get(1), equalTo("world"));
    }

    @Test
    public void doesNotCollectProcessOutputUnlessToldTo() throws IOException, InterruptedException {
        ExternalProcess process = Command.parse("src/test/resources/myscript.sh")
                .start();
        process.waitFor();

        assertThat(process.getOutput().isEmpty(), is(true));
    }
}
