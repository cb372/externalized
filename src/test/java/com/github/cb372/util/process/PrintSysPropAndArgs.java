package com.github.cb372.util.process;

/**
 * Author: chris
 * Created: 4/9/13
 */
public class PrintSysPropAndArgs {
    public static void main(String[] args) {
        System.out.println(System.getProperty("foo"));
        for (String arg : args) {
            System.out.println(arg);
        }
    }
}
