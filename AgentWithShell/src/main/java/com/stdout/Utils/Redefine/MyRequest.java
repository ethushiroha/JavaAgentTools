package com.stdout.Utils.Redefine;

import java.lang.reflect.Method;

public class MyRequest {
    public static String getParameter(Object request, String name) throws Exception{
        Method m = request.getClass().getMethod("getParameter", String.class);
        m.setAccessible(true);
        return (String) m.invoke(request, name);
    }

    public static String getHeader(Object request, String name) throws Exception {
        Method m = request.getClass().getMethod("getHeader", String.class);
        m.setAccessible(true);
        return (String) m.invoke(request, name);
    }

    public static Object getSession(Object request) throws Exception {
        Method m = request.getClass().getMethod("getSession", null);
        m.setAccessible(true);
        return m.invoke(request, new Object[] {});
    }

    public static int getContentLength(Object request) throws Exception {
        Method m = request.getClass().getMethod("getContentLength", null);
        m.setAccessible(true);
        return Integer.parseInt(m.invoke(request, new Object[] {}).toString());
    }

    public static Object getInputStream(Object request) throws Exception {
        Method m = request.getClass().getMethod("getInputStream", null);
        m.setAccessible(true);
        return m.invoke(request, new Object[] {});
    }

    public static String getRequestURI(Object request) throws Exception {
//        javax.servlet.http.HttpServletRequest t = (javax.servlet.http.HttpServletRequest)request;

        Method m = request.getClass().getMethod("getRequestURI", null);
        m.setAccessible(true);
        return (String) m.invoke(request, new Object[] {});
//        return t.getRequestURI();
    }

}
