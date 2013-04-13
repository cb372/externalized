package com.github.cb372.util.process;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
    public T usingJavaHome(File javaHome) {
        this.javaHome = javaHome;
        return subclass.cast(this);
    }

    /**
     * Add an argument. This will be passed to the main(String[] args) method.
     *
     * @param arg argument
     * @return builder
     */
    public T withArg(String arg) {
        return withArgs(arg);
    }

    /**
     * Add one or more arguments. This will be passed to the main(String[] args) method.
     *
     * @param args arguments
     * @return builder
     */
    public T withArgs(List<String> args) {
        this.args.addAll(args);
        return subclass.cast(this);
    }

    /**
     * Add one or more arguments. This will be passed to the main(String[] args) method.
     *
     * @param args arguments
     * @return builder
     */
    public T withArgs(String... args) {
        return withArgs(Arrays.asList(args));
    }

    /**
     * Add a JVM argument
     * @param arg JVM arg. e.g. "-Xm512m"
     * @return builder
     */
    public T withJvmArg(String arg) {
        return withJvmArgs(arg);
    }

    /**
     * Add one or more JVM arguments
     *
     * @param args JVM arguments
     * @return builder
     */
    public T withJvmArgs(List<String> args) {
        this.jvmArgs.addAll(args);
        return subclass.cast(this);
    }

    /**
     * Add one or more JVM arguments
     *
     * @param args JVM arguments
     * @return builder
     */
    public T withJvmArgs(String... args) {
        return withJvmArgs(Arrays.asList(args));
    }

    /**
     * Add a system property to pass to the JVM. Will be passed as "-Dkey=value".
     * @param key key
     * @param value value
     * @return builder
     */
    public T withSysProp(String key, String value) {
        jvmArgs.add(String.format("-D%s=%s", key, value));
        return subclass.cast(this);
    }

    /**
     * Replace the classpath with the given string
     * @param classpath the new classpath
     * @return builder
     */
    public T setClasspath(String classpath) {
        this.classpath = classpath;
        return subclass.cast(this);
    }

    /**
     * Add a jar or directory to the classpath
     * @param element a classpath element, usually a jar file or a directory.
     * @return builder
     */
    public T withClasspathElement(String element) {
        classpath = appendClasspathElement(classpath, element);
        return subclass.cast(this);
    }

    /**
     * Add a jar or directory to the classpath
     * @param element a classpath element, usually a jar file or a directory.
     * @return builder
     */
    public T withClasspathElement(File element) {
        classpath = appendClasspathElement(classpath, element.getAbsolutePath());
        return subclass.cast(this);
    }

    private String appendClasspathElement(String classpath, String element) {
        if (classpath.isEmpty()) {
            return element;
        } else {
            return classpath + File.pathSeparator + element;
        }
    }

    /**
     * Set the class that should be run. This class must have a public static void main(String[]) method.
     * @param mainClass fully qualified class name, e.g. "com.foo.Bar"
     * @return builder
     */
    public WithMainClass mainClass(String mainClass) {
        return new WithMainClass(javaHome, jvmArgs, args, classpath, mainClass);
    }

    /**
     * Set the class that should be run. This class must have a public static void main(String[]) method.
     * @param mainClass main class
     * @return builder
     */
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


