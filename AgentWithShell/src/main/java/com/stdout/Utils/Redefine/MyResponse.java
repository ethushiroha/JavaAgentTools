package com.stdout.Utils.Redefine;

import java.io.PrintWriter;

public class MyResponse {
    public static void setContentType(Object response, String args) throws Exception {
        response.getClass().getMethod("setContentType", String.class).invoke(response, args);
    }

    public static void setCharacterEncoding(Object response, String args) throws Exception {
        response.getClass().getMethod("setCharacterEncoding", String.class).invoke(response, args);
    }

    public static PrintWriter getWriter(Object response) throws Exception {
        return (PrintWriter) response.getClass().getMethod("getWriter", null).invoke(response, new Object[] {});
    }

    public static Object getOutputStream(Object response) throws Exception {
        return response.getClass().getMethod("getOutputStream", null).invoke(response, new Object[] {});
    }

    public static void setHeader(Object response, String arg1, String arg2) throws Exception {
        response.getClass().getMethod("setHeader", String.class, String.class).invoke(response, arg1, arg2);
    }

    public static void resetBuffer(Object response) throws Exception {
        response.getClass().getMethod("resetBuffer", null).invoke(response, new Object[] {});
    }

    public static void setStatus(Object response, int code) throws Exception {
        response.getClass().getMethod("setStatus", int.class).invoke(response, code);
    }
}
