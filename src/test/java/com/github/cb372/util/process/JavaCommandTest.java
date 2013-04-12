package com.github.cb372.util.process;

import org.junit.Test;

import java.io.IOException;

import static com.github.cb372.util.process.Command.command;
import static com.github.cb372.util.process.Java.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Author: chris
 * Created: 4/9/13
 */
public class JavaCommandTest {

    @Test
    public void canRunAJavaClassInASeparateProcess() throws IOException, InterruptedException {
        ExternalProcess process = command(java().mainClass(PrintHello.class))
                .collectStdOut()
                .start();

        assertThat(process.getOutput().get(0), is("hello"));
    }

    @Test
    public void argumentsAndSysPropsArePassedToProcess() throws IOException, InterruptedException {
        ExternalProcess process = command(java()
                .sysProp("foo", "bar")
                .mainClass(PrintSysPropAndArgs.class)
                .arg("hello")
                .arg("world"))
                .collectStdOut()
                .start();

        assertThat(process.getOutput().get(0), is("bar"));
        assertThat(process.getOutput().get(1), is("hello"));
        assertThat(process.getOutput().get(2), is("world"));
    }
}
