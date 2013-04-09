package com.github.cb372.util.process;

/**
 * DSL for building Java commands.
 *
 * Author: chris
 * Created: 4/8/13
 */
public final class Java {

    /**
     * Start building a command to run the given class in a separate process.
     *
     * @param mainClass fully qualified class name. Class must have a public static void main method.
     * @return builder
     */
    public static JavaCommandBuilder mainClass(String mainClass) {
        return new JavaCommandBuilder(mainClass);
    }

    /**
     * Start building a command to run the given class in a separate process.
     *
     * @param mainClass class to run. Class must have a public static void main method.
     * @return builder
     */
    public static JavaCommandBuilder mainClass(Class<?> mainClass) {
        return new JavaCommandBuilder(mainClass.getCanonicalName());
    }
}
