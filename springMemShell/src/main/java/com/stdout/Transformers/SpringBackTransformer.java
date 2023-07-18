package com.stdout.Transformers;

import com.stdout.Config.DefaultConfig;
import com.stdout.Utils.Changer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class SpringBackTransformer implements ClassFileTransformer {
    public static final String TransformedClassName = DefaultConfig.SpringMemTransformerConfig.TransformedClassName;
    public static final String TransformedEqualName = DefaultConfig.SpringMemTransformerConfig.TransformedEqualName;
    public static final String TransformedMethodName = DefaultConfig.SpringMemTransformerConfig.TransformedMethodName;

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals(TransformedEqualName)) {
            try {
                return Changer.ChangeSource(classBeingRedefined, TransformedClassName, TransformedMethodName, DefaultConfig.TemplatesConfig.EndTransformer, false);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
