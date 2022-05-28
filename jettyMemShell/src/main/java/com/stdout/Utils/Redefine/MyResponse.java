package com.stdout.Utils.Redefine;

import java.io.PrintWriter;

public class MyResponse {
    public static void setContentType(Object response, String args) throws Exception {
        response.getClass().getDeclaredMethod("setContentType", String.class).invoke(response, args);
    }

    public static void setCharacterEncoding(Object response, String args) throws Exception {
        response.getClass().getDeclaredMethod("setCharacterEncoding", String.class).invoke(response, args);
    }

    public static PrintWriter getWriter(Object response) throws Exception {
        return (PrintWriter) response.getClass().getDeclaredMethod("getWriter", null).invoke(response, new Object[] {});
    }

    public static Object getOutputStream(Object response) throws Exception {
        return response.getClass().getDeclaredMethod("getOutputStream", null).invoke(response, new Object[] {});
    }

    public static void setHeader(Object response, String arg1, String arg2) throws Exception {
        response.getClass().getDeclaredMethod("setHeader", String.class, String.class).invoke(response, arg1, arg2);
    }

    public static void resetBuffer(Object response) throws Exception {
        response.getClass().getDeclaredMethod("resetBuffer", null).invoke(response, new Object[] {});
    }

    public static void setStatus(Object response, int code) throws Exception {
        response.getClass().getDeclaredMethod("setStatus", int.class).invoke(response, code);
    }
}
