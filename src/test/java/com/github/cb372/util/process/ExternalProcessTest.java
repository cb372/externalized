package com.github.cb372.util.process;


import com.github.cb372.util.stream.listener.OutputCollector;
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
        OutputCollector stdoutListener = new OutputCollector();
        OutputCollector stderrListener = new OutputCollector();

        ExternalProcess process = Command.parse("src/test/resources/myscript.sh")
                .processStdOut(StreamProcessing.consume().withListener(stdoutListener))
                .processStdErr(StreamProcessing.consume().withListener(stderrListener))
                .start();

        process.waitFor();
        assertThat(stdoutListener.awaitCompletion(1, TimeUnit.SECONDS), is(true));
        assertThat(stderrListener.awaitCompletion(1, TimeUnit.SECONDS), is(true));

        assertThat(stdoutListener.get().get(0), equalTo("hello"));
        assertThat(stdoutListener.get().get(1), equalTo("world"));
        assertThat(stderrListener.get().get(0), equalTo("oh noes"));
    }

    @Test
    public void canRedirectStderrToStdout() throws IOException, InterruptedException {
        OutputCollector stdoutListener = new OutputCollector();

        ExternalProcess process = Command.parse("src/test/resources/myscript.sh")
                .processStdOut(StreamProcessing.consume().withListener(stdoutListener))
                .redirectingErrorStream()
                .start();

        process.waitFor();
        assertThat(stdoutListener.awaitCompletion(1, TimeUnit.SECONDS), is(true));

        assertThat(stdoutListener.get().get(0), equalTo("hello"));
        assertThat(stdoutListener.get().get(1), equalTo("world"));
        assertThat(stdoutListener.get().get(2), equalTo("oh noes"));
    }

    @Test
    public void canSetEnvironmentVariables() throws IOException, InterruptedException {
        OutputCollector stdoutListener = new OutputCollector();

        ExternalProcess process = Command.parse("src/test/resources/echo-foo.sh")
                .withEnvVar("FOO", "bar")
                .processStdOut(StreamProcessing.consume().withListener(stdoutListener))
                .start();
        process.waitFor();

        assertThat(stdoutListener.awaitCompletion(1, TimeUnit.SECONDS), is(true));
        assertThat(stdoutListener.get().get(0), equalTo("bar"));
    }

}
