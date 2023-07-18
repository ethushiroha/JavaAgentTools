package com.stdout.Models;

import java.io.IOException;

public class CommandExecutor {
    public static String exec(String cmd) throws IOException {
        String result = "";
        if (cmd != null && cmd.length() > 0) {
            Process p = Runtime.getRuntime().exec(cmd);
            java.io.OutputStream os = p.getOutputStream();
            java.io.InputStream in = p.getInputStream();
            java.io.DataInputStream dis = new java.io.DataInputStream(in);

            for (String disr = dis.readLine(); disr != null; disr = dis.readLine()) {
                result = result + disr + "\n";
            }
        }
        return result;
    }

}
