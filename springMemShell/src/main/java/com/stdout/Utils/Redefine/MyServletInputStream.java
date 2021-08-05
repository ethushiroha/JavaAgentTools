package com.stdout.Utils.Redefine;

public class MyServletInputStream {
    public static void read(Object servlet, byte[] a, int b, int c) throws Exception {
        servlet.getClass().getDeclaredMethod("read", byte[].class, int.class, int.class).invoke(servlet, a, b ,c);
    }
}
