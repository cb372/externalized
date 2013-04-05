package com.github.cb372.util.sample;

import com.github.cb372.util.process.Command;
import com.github.cb372.util.process.ExternalProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.github.cb372.util.process.Logging.usingLogger;
import static com.github.cb372.util.process.StreamProcessing.gobble;

/**
 * Author: chris
 * Created: 4/5/13
 */
public class ExternalProcessSample {
    private final Logger myLogger = LoggerFactory.getLogger("myLogger");
    private final Logger myErrorLogger = LoggerFactory.getLogger("myErrorLogger");

    public void example() throws IOException, InterruptedException {
        // Use the DSL to build a process
        ExternalProcess process = Command.parse("myscript.sh -a -b -c foo bar")
                .withEnvVar("WIBBLE", "wobble")
                .withEnvVar("FUNKY", "monkey")
                .withWorkingDirectory(new File("/tmp"))
                .processStdOut(gobble()
                        .withCharset("UTF-8")
                        .pipingToStdOut() // pipe all process output to our stdout/stderr
                        .withLogging(usingLogger(myLogger).atInfoLevel().withPrefix("Script output: ")) // log process output
                )
                .processStdErr(gobble()
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

}
