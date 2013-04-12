package com.github.cb372.util.process;

/**
 * DSL for building Java commands.
 *
 * Author: chris
 * Created: 4/8/13
 */
public final class Java {

    public static JavaCommandBuilder java() {
        return new JavaCommandBuilder.WithoutMainClass();
    }

}
