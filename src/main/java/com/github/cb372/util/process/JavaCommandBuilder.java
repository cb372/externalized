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

    public JavaCommandBuilder javaHome(File javaHome) {
        this.javaHome = javaHome;
        return this;
    }

    public JavaCommandBuilder arg(String arg) {
        args.add(arg);
        return this;
    }

    public JavaCommandBuilder jvmArg(String arg) {
        jvmArgs.add(arg);
        return this;
    }

    public JavaCommandBuilder sysProp(String key, String value) {
        jvmArgs.add(String.format("-D%s=%s", key, value));
        return this;
    }

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
