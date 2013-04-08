package com.github.cb372.util.process;

/**
 * Author: chris
 * Created: 4/8/13
 */
public final class Java {

    public static JavaCommandBuilder mainClass(String mainClass) {
        return new JavaCommandBuilder(mainClass);
    }

    public static JavaCommandBuilder mainClass(Class<?> mainClass) {
        return new JavaCommandBuilder(mainClass.getCanonicalName());
    }
}
