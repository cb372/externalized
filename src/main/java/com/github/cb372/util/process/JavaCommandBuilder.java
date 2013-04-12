package com.github.cb372.util.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: chris
 * Created: 4/8/13
 */
public abstract class JavaCommandBuilder<T extends JavaCommandBuilder<T>> {
    private final Class<T> subclass;

    protected File javaHome = new File(System.getProperty("java.home"));
    protected List<String> jvmArgs = new ArrayList<String>();
    protected List<String> args = new ArrayList<String>();
    protected String classpath = System.getProperty("java.class.path");

    protected JavaCommandBuilder(Class<T> subclass) {
        this.subclass = subclass;
    }

    protected JavaCommandBuilder(Class<T> subclass, File javaHome, List<String> jvmArgs, List<String> args, String classpath) {
        this(subclass);
        this.javaHome = javaHome;
        this.jvmArgs = jvmArgs;
        this.args = args;
        this.classpath = classpath;
    }

    /**
     * Set the JDK/JRE to run.
     *
     * By default the currently running JRE is used.
     *
     * @param javaHome Java home dir
     * @return builder
     */
    public T javaHome(File javaHome) {
        this.javaHome = javaHome;
        return subclass.cast(this);
    }

    /**
     * Add an argument to the class. This will be passed to the main(String[] args) method.
     *
     * @param arg
     * @return builder
     */
    public T arg(String arg) {
        args.add(arg);
        return subclass.cast(this);
    }

    /**
     * Add a JVM argument
     * @param arg JVM arg. e.g. "-Xm512m"
     * @return builder
     */
    public T jvmArg(String arg) {
        jvmArgs.add(arg);
        return subclass.cast(this);
    }

    /**
     * Add a system property to pass to the JVM. Will be passed as "-Dkey=value".
     * @param key key
     * @param value value
     * @return builder
     */
    public T sysProp(String key, String value) {
        jvmArgs.add(String.format("-D%s=%s", key, value));
        return subclass.cast(this);
    }

    public WithMainClass mainClass(String mainClass) {
        return new WithMainClass(javaHome, jvmArgs, args, classpath, mainClass);
    }

    public WithMainClass mainClass(Class<?> mainClass) {
        return new WithMainClass(javaHome, jvmArgs, args, classpath, mainClass.getCanonicalName());
    }

    static final class WithoutMainClass extends JavaCommandBuilder<WithoutMainClass> {
        protected WithoutMainClass() {
            super(WithoutMainClass.class);
        }
    }

    public static final class WithMainClass extends JavaCommandBuilder<WithMainClass> implements ProcessBuilderProvider {
        private String mainClass;

        public WithMainClass(File javaHome,
                             List<String> jvmArgs,
                             List<String> args,
                             String classpath,
                             String mainClass) {
            super(WithMainClass.class, javaHome, jvmArgs, args, classpath);
            this.mainClass = mainClass;
        }

        @Override
        public WithMainClass mainClass(String mainClass) {
            this.mainClass = mainClass;
            return this;
        }

        @Override
        public WithMainClass mainClass(Class<?> mainClass) {
            this.mainClass = mainClass.getCanonicalName();
            return this;
        }

        @Override
        public ProcessBuilder getProcessBuilder() {
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

}


