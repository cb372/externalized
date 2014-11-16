0.4.0 (2014/11/16)
----

Features:

* Added ability to prefix all stdout/stderr output with a custom string (rodionmoiseev)

Breaking changes:

* Ended support for Java 6, as it is EOL.

0.3.0 (2013/09/29)
----

Features:

* Added support for processing stdout/stderr as binary rather than text

Breaking changes:

* The DSL for processing stdout/stderr has changed slightly.
    * Before: `processStdOut(consume().withListener(...))`
    * After: `processStdOut(consume().asText().withListener(...))`

* Class name change: `ExternalProcessBuilder` used to return an `ExternalProcess`. It now returns a `TextCollectingExternalProcess`.

0.2.0 (2013/04/13)
----

Features:

* Added a DSL extension for running Java processes
* Made it easier to collect output from a process

0.1.0 (2013/04/06)
-----

First release of DSL and stream listeners.
