package com.stdout.Utils.Redefine;

public class MyServletOutputStream {
    public static void write(Object servlet, byte[] a, int b, int c) throws Exception {
        servlet.getClass().getMethod("write", byte[].class, int.class, int.class).invoke(servlet, a, b, c);
    }

    public static void close(Object servlet) throws Exception {
        servlet.getClass().getMethod("close", null).invoke(servlet, new Object[] {});
    }

    public static void flush(Object servlet) throws Exception {
        servlet.getClass().getMethod("flush", null).invoke(servlet, new Object[] {});
    }
}
