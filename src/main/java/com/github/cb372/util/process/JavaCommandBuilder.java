package com.github.cb372.util.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: chris
 * Created: 4/8/13
 */
public class JavaCommandBuilder {

    private final String mainClass;
    private File javaHome = new File(System.getProperty("java.home"));
    private List<String> args = new ArrayList<String>();
    private List<String> jvmArgs = new ArrayList<String>();
    private String classpath = System.getProperty("java.class.path");

    protected JavaCommandBuilder(String mainClass) {
        this.mainClass = mainClass;
    }

    /**
     * Set the JDK/JRE to run.
     *
     * By default the currently running JRE is used.
     *
     * @param javaHome Java home dir
     * @return builder
     */
    public JavaCommandBuilder javaHome(File javaHome) {
        this.javaHome = javaHome;
        return this;
    }

    /**
     * Add an argument to the class. This will be passed to the main(String[] args) method.
     *
     * @param arg
     * @return builder
     */
    public JavaCommandBuilder arg(String arg) {
        args.add(arg);
        return this;
    }

    /**
     * Add a JVM argument
     * @param arg JVM arg. e.g. "-Xm512m"
     * @return builder
     */
    public JavaCommandBuilder jvmArg(String arg) {
        jvmArgs.add(arg);
        return this;
    }

    /**
     * Add a system property to pass to the JVM. Will be passed as "-Dkey=value".
     * @param key key
     * @param value value
     * @return builder
     */
    public JavaCommandBuilder sysProp(String key, String value) {
        jvmArgs.add(String.format("-D%s=%s", key, value));
        return this;
    }

    /**
     * Build the Java command.
     * @return
     */
    protected ProcessBuilder build() {
        List<String> cmd = new ArrayList<String>();
        cmd.add(new File(javaHome, "bin/java").getAbsolutePath());
        cmd.add("-cp");
        cmd.add(classpath);
        for (String arg : jvmArgs) {
            cmd.add(arg);
        }
        cmd.add(mainClass);
        for (String arg : args) {
            cmd.add(arg);
        }
        return new ProcessBuilder(cmd);
    }

}
