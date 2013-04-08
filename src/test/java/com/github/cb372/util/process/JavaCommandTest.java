package com.github.cb372.util.process;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Author: chris
 * Created: 4/9/13
 */
public class JavaCommandTest {

    @Test
    public void canRunAJavaClassInASeparateProcess() throws IOException, InterruptedException {
        ExternalProcess process = Command.java(Java.mainClass(Foo.class))
                .collectStdOut()
                .start();

        assertThat(process.getOutput().get(0), is("hello"));
    }
}
