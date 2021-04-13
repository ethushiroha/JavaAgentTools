package com.stdout.Utils.Redefine;

import java.lang.reflect.Method;

public class MyRequest {
    public static String getParameter(Object request, String name) throws Exception{
        Method m = request.getClass().getDeclaredMethod("getParameter", String.class);
        m.setAccessible(true);
        return (String) m.invoke(request, name);
    }

    public static Object getServletContext(Object request) throws Exception {
        Method m = request.getClass().getDeclaredMethod("getServletContext", null);
        m.setAccessible(true);
        return m.invoke(request, new Object[] {});
    }

    public static String getHeader(Object request, String name) throws Exception {
        Method m = request.getClass().getDeclaredMethod("getHeader", String.class);
        m.setAccessible(true);
        return (String) m.invoke(request, name);
    }

    public static Object getSession(Object request) throws Exception {
        Method m = request.getClass().getDeclaredMethod("getSession", null);
        m.setAccessible(true);
        return m.invoke(request, new Object[] {});
    }

    public static int getContentLength(Object request) throws Exception {
        Method m = request.getClass().getDeclaredMethod("getContentLength", null);
        m.setAccessible(true);
        return Integer.parseInt(m.invoke(request, new Object[] {}).toString());
    }

    public static Object getInputStream(Object request) throws Exception {
        Method m = request.getClass().getDeclaredMethod("getInputStream", null);
        m.setAccessible(true);
        return m.invoke(request, new Object[] {});
    }

    public static String getRequestURI(Object request) throws Exception {
        Method m = request.getClass().getDeclaredMethod("getRequestURI", null);
        m.setAccessible(true);
        return (String) m.invoke(request, new Object[] {});
    }

    public static String getMethod(Object request) throws Exception {
        Method m = request.getClass().getDeclaredMethod("getMethod", null);
        m.setAccessible(true);
        return (String) m.invoke(request, new Object[] {});
    }

    public static Object getReader(Object request) throws Exception {
        Method m = request.getClass().getDeclaredMethod("getReader", null);
        m.setAccessible(true);
        return m.invoke(request, new Object[] {});
    }

}
