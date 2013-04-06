Externalized
=====

A handy DSL to make it easier to work with external processes in Java.

Sample
-----

````java
ExternalProcess process = Command.parse("myscript.sh -a -b -c foo bar")
        .withEnvVar("WIBBLE", "wobble") // add environment variables to the process's environment
        .withWorkingDirectory(new File("/tmp")) // set the working directory
        .processStdOut(consume()
                .withCharset("UTF-8")
                .pipingToStdOut() // pipe process's stdout to our own stdout
        )
        .processStdErr(consume()
                .withCharset("UTF-8")
                .withLogging(usingLogger(myErrorLogger).atErrorLevel().withPrefix("Error in script!! - "))
        )
        .start();
````

See [here](src/test/java/com/github/cb372/util/sample/ExternalProcessSample.java) for a more detailed example.

Listeners
-----

You can register one or more listeners to be notified of data received on a process's stdout or stderr.

The following `StreamListener` implementations are provided:

* `OutputCollectingListener` - saves all output from a given stream as a `List<String>`
* `LoggingListener` - pipes all output to an Slf4j logger of your choosing
* `PipingListener` - pipes all output to our own stdout/stderr

You can easily provide your own custom listeners. Just write an implementation of [StreamListener](src/main/java/com/github/cb372/util/stream/listener/StreamListener.java) and use it as follows:

````java
ExternalProcess process = Command.parse("foo bar baz")
        .processStdOut(consume().withListener(myCustomListener))
        .start();
````

Usage
-----

Available on Maven central.

Maven:

````
<dependency>
  <groupId>com.github.cb372</groupId>
  <artifactId>externalized</artifactId>
  <version>0.1.0</version>
</dependency>
````

Dependencies
-----

* Java 6 or newer
* slf4j API

Licence
-----

Apache 2.0
