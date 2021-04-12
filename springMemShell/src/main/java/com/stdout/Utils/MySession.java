package com.stdout.Utils;

public class MySession {
    public static void setAttribute(Object httpSession, String arg1, Object arg2) throws Exception {
        httpSession.getClass().getDeclaredMethod("setAttribute", String.class, Object.class).invoke(httpSession, arg1, arg2);
    }

    public static Object getAttribute(Object httpSession, String arg) throws Exception {
        return httpSession.getClass().getDeclaredMethod("getAttribute", String.class).invoke(httpSession, arg);
    }

    public static void invalidate(Object httpSession) throws Exception {
        httpSession.getClass().getDeclaredMethod("invalidate", null).invoke(httpSession, new Object[] {});
    }

    public static void removeAttribute(Object session, String arg1) throws Exception {
        session.getClass().getDeclaredMethod("removeAttribute", String.class).invoke(session, arg1);
    }
}
