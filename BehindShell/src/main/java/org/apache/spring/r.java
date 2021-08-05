package org.apache.spring;

import java.lang.reflect.Method;

public class r {
    public static String getParameter(Object request, String name) throws Exception{
        Method m = request.getClass().getDeclaredMethod("getParameter", String.class);
        m.setAccessible(true);
        return (String) m.invoke(request, name);
    }

    public static String getRequestURI(Object request) throws Exception {
        Method m = request.getClass().getDeclaredMethod("getRequestURI", null);
        m.setAccessible(true);
        return (String) m.invoke(request, new Object[] {});
    }

    public static String getMethod(Object request) throws Exception {
        return (String) request.getClass().getMethod("getMethod", null).invoke(request, new Object[]{});
    }

    public static Object getSession(Object request) throws Exception {
        return request.getClass().getDeclaredMethod("getSession",null).invoke(request, new Object[] {});
    }

    public static Object getReader(Object request) throws Exception {
        return request.getClass().getDeclaredMethod("getReader", null).invoke(request, new Object[] {});
    }

    // reader
    public static String readLine(Object reader) throws Exception {
        return (String) reader.getClass().getDeclaredMethod("readLine", null).invoke(reader, new Object[] {});
    }
}
