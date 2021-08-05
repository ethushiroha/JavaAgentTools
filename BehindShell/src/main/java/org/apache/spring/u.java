package org.apache.spring;


import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

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

    public static byte[] ChangeSource(Class<?> aClass, String ClassName, String MethodName, String Path) throws Exception {
        try {
            ClassPool cp = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(aClass);
            cp.insertClassPath(classPath);

            CtClass cc = cp.get(ClassName);
            CtMethod m = cc.getDeclaredMethod(MethodName);
            m.insertBefore(u.readSource(c.TemplatesConfig.StartTransformer));
            byte[] bytes = cc.toBytecode();
            cc.detach();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
