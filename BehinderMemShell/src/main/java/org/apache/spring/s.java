package org.apache.spring;

public class s {
    public static void putValue(Object session, String u, Object k) throws Exception {
        session.getClass().getDeclaredMethod("putValue", new Class[] {String.class, Object.class}).invoke(session, new Object[] {u, k});
    }
}
