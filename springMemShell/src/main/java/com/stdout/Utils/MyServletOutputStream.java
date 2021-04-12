package com.stdout.Utils;

public class MyServletOutputStream {
    public static void write(Object servlet, byte[] a, int b, int c) throws Exception {
        servlet.getClass().getDeclaredMethod("write", byte[].class, int.class, int.class).invoke(servlet, a, b, c);
    }

    public static void close(Object servlet) throws Exception {
        servlet.getClass().getDeclaredMethod("close", null).invoke(servlet, new Object[] {});
    }

    public static void flush(Object servlet) throws Exception {
        servlet.getClass().getDeclaredMethod("flush", null).invoke(servlet, new Object[] {});
    }
}
