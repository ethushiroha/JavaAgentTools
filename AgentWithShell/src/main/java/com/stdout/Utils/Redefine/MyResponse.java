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

    public static void setHeader(Object response, String key, String value) throws Exception {
        response.getClass().getMethod("setHeader", String.class, String.class).invoke(response, key, value);
    }

    public static void resetBuffer(Object response) throws Exception {
        response.getClass().getMethod("resetBuffer", null).invoke(response, new Object[] {});
    }

    public static void setStatus(Object response, int code) throws Exception {
        response.getClass().getMethod("setStatus", int.class).invoke(response, code);
    }

    public static void setBufferSize(Object response, int size) throws Exception {
        response.getClass().getMethod("setBufferSize", int.class).invoke(response, size);
    }

    public static void flushBuffer(Object response) throws Exception {
        response.getClass().getMethod("flushBuffer", null).invoke(response, new Object[] {});
    }
}
