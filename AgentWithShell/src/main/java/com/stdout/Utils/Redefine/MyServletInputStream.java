package com.stdout.Utils.Redefine;

import java.lang.reflect.Method;

public class MyServletInputStream {
    public static int read(Object servlet, byte[] a, int b, int c) throws Exception {
        try {
            Method m = servlet.getClass().getMethod("read", byte[].class, int.class, int.class);
            m.setAccessible(true);
            return (int) m.invoke(servlet, a, b, c);
        }catch (Exception e) {
//            e.printStackTrace();
        }
        return -1;
    }
}
