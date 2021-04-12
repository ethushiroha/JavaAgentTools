package com.stdout.Utils;

import java.io.ByteArrayOutputStream;

public class B64 {
    private static char[] en = "1nAo76ptVK5Ja/3gSuErjTqQOmkvyY9XGMdRFzCZDUHPl8f2BhIwxciN4L+0bsWe".toCharArray();
    public static String b64en(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(en[b1 >>> 2]);
                sb.append(en[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(en[b1 >>> 2]);
                sb.append(en[((b1 & 0x03) << 4)
                        | ((b2 & 0xf0) >>> 4)]);
                sb.append(en[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(en[b1 >>> 2]);
            sb.append(en[((b1 & 0x03) << 4)
                    | ((b2 & 0xf0) >>> 4)]);
            sb.append(en[((b2 & 0x0f) << 2)
                    | ((b3 & 0xc0) >>> 6)]);
            sb.append(en[b3 & 0x3f]);
        }
        return sb.toString();
    }
    private static byte[] de = new byte[] {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,58,-1,-1,-1,13,59,0,47,14,56,10,5,4,45,30,-1,-1,-1,-1,-1,-1,-1,2,48,38,40,18,36,32,42,50,11,9,57,33,55,24,43,23,35,16,21,41,8,62,31,29,39,-1,-1,-1,-1,-1,-1,12,60,53,34,63,46,15,49,54,20,26,44,25,1,3,6,22,19,61,7,17,27,51,52,28,37,-1,-1,-1,-1,-1};
    public static byte[] b64de(String str) {
        byte[] data = str.getBytes();
        int len = data.length;
        ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
        int i = 0;
        int b1, b2, b3, b4;
        while (i < len) {
            do {
                b1 = de[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1) {
                break;
            }
            do {
                b2 = de[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1) {
                break;
            }
            buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
            do {
                b3 = data[i++];
                if (b3 == 61) {
                    return buf.toByteArray();
                }
                b3 = de[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1) {
                break;
            }
            buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
            do {
                b4 = data[i++];
                if (b4 == 61) {
                    return buf.toByteArray();
                }
                b4 = de[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1) {
                break;
            }
            buf.write((int) (((b3 & 0x03) << 6) | b4));
        }
        return buf.toByteArray();
    }
}
