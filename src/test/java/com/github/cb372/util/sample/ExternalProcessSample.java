package com.github.cb372.util.sample;

import com.github.cb372.util.process.ExternalProcess;
import com.github.cb372.util.process.PrintHello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.github.cb372.util.process.Command.parse;
import static com.github.cb372.util.process.Command.command;
import static com.github.cb372.util.process.Java.java;
import static com.github.cb372.util.process.Logging.usingLogger;
import static com.github.cb372.util.process.StreamProcessing.consume;

/**
 * Author: chris
 * Created: 4/5/13
 */
public class ExternalProcessSample {
    private final Logger myLogger = LoggerFactory.getLogger("myLogger");
    private final Logger myErrorLogger = LoggerFactory.getLogger("myErrorLogger");

    public void example1() throws IOException, InterruptedException {
        /*
         * Example 1: Detailed configuration
         */

        // Use the DSL to build a process
        ExternalProcess process = parse("myscript.sh -a -b -c foo bar")
                .withEnvVar("WIBBLE", "wobble") // add environment variables to the process's environment
                .withEnvVar("FUNKY", "monkey")
                .withWorkingDirectory(new File("/tmp")) // set the working directory
                .processStdOut(consume()
                        .withCharset("UTF-8")
                        .pipingToStdOut() // pipe all process output to our own stdout/stderr
                        .withLogging(usingLogger(myLogger).atInfoLevel().withPrefix("Script output: ")) // log process output
                )
                .processStdErr(consume()
                        .withCharset("UTF-8")
                        .withLogging(usingLogger(myErrorLogger).atErrorLevel().withPrefix("Error in script!! - "))
                        .withListener(new MyCustomErrorListener()) // add your own custom listeners
                        .pipingToStdErr()
                        .inDaemonThread() // set detailed config for stream processing threads
                        .withThreadName("StdErrorConsumer")
                        .withThreadPriority(10)
                        .withUncaughtExceptionHandler(new MyUncaughtExceptionHandler())
                        .withIOExceptionHandler(new MyIOExceptionHandler())
                )
                .start();

        // You can treat ExternalProcess just like java.lang.Process
        int exitCode = process.waitFor();
        System.out.println("Process finished with exit code " + exitCode);
    }

    public void example2() throws IOException, InterruptedException {
        /*
         * Example 2: Collecting output from the process
         */

        // Run a process
        ExternalProcess process = command("myscript.sh")
                .collectStdOut()  // collect all data that the process sends to stdout
                .start();

        // After the process has finished, you can access the data it sent to stdout
        String firstLineOfOutput = process.getOutput().get(0);
        if (firstLineOfOutput.equals("OK")) {
            System.out.println("Looks like the script ran fine!");
        }
    }

    public void example3() throws IOException {
        /*
         * Example 3: Running a Java class in a separate process
         */
        ExternalProcess process = command(java()
                .jvmArg("-Xmx512m")
                .sysProp("foo", "bar")
                .mainClass(PrintHello.class)
                .arg("hello")
                .arg("world"))
                .start();

    }
}
