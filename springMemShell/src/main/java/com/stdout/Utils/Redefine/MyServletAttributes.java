package com.stdout.Utils.Redefine;

public class MyServletAttributes {
    public static Object getRequest(Object servlet) throws Exception {
        return servlet.getClass().getDeclaredMethod("getRequest").invoke(servlet);
    }

    public static Object getResponse(Object servlet) throws Exception {
        return servlet.getClass().getDeclaredMethod("getResponse").invoke(servlet);
    }

    public static Object getSession(Object servlet) throws Exception {
        return servlet.getClass().getDeclaredMethod("getSession").invoke(servlet);
    }
}
