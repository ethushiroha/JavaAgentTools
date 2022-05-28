package com.stdout.Utils;

import com.stdout.Config.ShellConfig;
import com.stdout.Utils.Redefine.MyReader;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class Changer {
    public static byte[] ChangeSource(Class<?> aClass, String ClassName, String MethodName, String Path, boolean isInsertBefore) throws Exception{
        try {
            System.out.println("change start");
            ClassPool cp = ClassPool.getDefault();
            if (aClass != null) {
                ClassClassPath classPath = new ClassClassPath(aClass);
                cp.insertClassPath(classPath);
            }
            CtClass ctc = cp.get(ClassName);
            CtMethod method = ctc.getDeclaredMethod(MethodName);

            String code = MyReader.readSource(Path);
            System.out.println("get code");
            if (isInsertBefore) {
                System.out.println("insert before");
                method.insertBefore(code);
            } else {
                method.setBody(code);
            }
            byte[] bytes = ctc.toBytecode();
            System.out.println("to bytecode");
            ctc.detach();
            System.out.println("change fin");
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }
}
