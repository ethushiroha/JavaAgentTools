package com.stdout.Utils.Redefine;

import java.lang.reflect.Method;

public class MyServletInputStream {
    public static void read(Object servlet, byte[] a, int b, int c) throws Exception {
        servlet.getClass().getDeclaredMethod("read", byte[].class, int.class, int.class).invoke(servlet, a, b ,c);
    }

    public static int readLine(Object servlet, byte[]b ,int off, int len) throws Exception {
        Method m = servlet.getClass().getDeclaredMethod("readLine", byte[].class, int.class, int.class);
        m.setAccessible(true);
        return (int) m.invoke(servlet, b, off, len);
    }
}
