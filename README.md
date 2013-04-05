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

Usage
-----

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
