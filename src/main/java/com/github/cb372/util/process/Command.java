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

    /**
     * Pass a command line as a string. It will be split by spaces.
     *
     * @param commandLine A full command line, e.g. "myscript.sh foo bar"
     * @return a process builder
     */
    public static ExternalProcessBuilder parse(String commandLine) {
        return command(commandLine.split(" "));
    }

    /**
     * Pass a command and its arguments as a sequence of strings.
     *
     * @param command a command and its arguments, e.g. ["myscript.sh", "foo, "bar"]
     * @return a process builder
     */
    public static ExternalProcessBuilder command(String... command) {
        return new ExternalProcessBuilder(new ProcessBuilder(command));
    }

    /**
     * Pass a command and its arguments as a list of strings.
     *
     * @param command a command and its arguments, e.g. ["myscript.sh", "foo, "bar"]
     * @return a process builder
     */
    public static ExternalProcessBuilder command(List<String> command) {
        return new ExternalProcessBuilder(new ProcessBuilder(command));
    }

    /**
     * Pass a Java command. {@see {@link Java}}
     *
     * @param javaCommandBuilder Java command builder
     * @return a process builder
     */
    public static ExternalProcessBuilder java(JavaCommandBuilder javaCommandBuilder) {
        return new ExternalProcessBuilder(javaCommandBuilder.build());
    }

}
