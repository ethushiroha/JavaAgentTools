package com.stdout.Utils.Redefine;

import java.lang.reflect.Method;

public class MyServletOutputStream {
    public static void write(Object servlet, byte[] a, int b, int c) throws Exception {
        Method m = servlet.getClass().getMethod("write", byte[].class, int.class, int.class);
        m.setAccessible(true);
        m.invoke(servlet, a, b, c);
    }

    public static void close(Object servlet) throws Exception {
        Method m = servlet.getClass().getMethod("close", null);
        m.setAccessible(true);
        m.invoke(servlet, new Object[] {});
    }

    public static void flush(Object servlet) throws Exception {
        Method m = servlet.getClass().getMethod("flush", null);
        m.setAccessible(true);
        m.invoke(servlet, new Object[] {});
    }
}
