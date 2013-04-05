package com.github.cb372.util.process;

import java.util.List;

/**
 * The entry point to the process-building DSL.
 *
 * Pass the command line for the process that you want to run.
 *
 * Author: chris
 * Created: 4/5/13
 */
public final class Command {

    public static ExternalProcessBuilder parse(String commandLine) {
        return command(commandLine.split(" "));
    }

    public static ExternalProcessBuilder command(String... command) {
        return new ExternalProcessBuilder(new ProcessBuilder(command));
    }

    public static ExternalProcessBuilder command(List<String> command) {
        return new ExternalProcessBuilder(new ProcessBuilder(command));
    }

}
