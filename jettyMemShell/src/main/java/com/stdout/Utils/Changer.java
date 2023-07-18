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
            ClassPool cp = ClassPool.getDefault();
            if (aClass != null) {
                ClassClassPath classPath = new ClassClassPath(aClass);
                cp.insertClassPath(classPath);
            }
            CtClass ctc = cp.get(ClassName);
            CtMethod method = ctc.getDeclaredMethod(MethodName);

            String code = MyReader.readSource(Path);
            if (isInsertBefore) {
                method.insertBefore(code);
            } else {
                method.setBody(code);
            }
            byte[] bytes = ctc.toBytecode();
            ctc.detach();
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }
}
