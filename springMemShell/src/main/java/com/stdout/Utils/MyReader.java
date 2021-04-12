package com.stdout.Utils;

import com.stdout.springMem.SpringMemTransformer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyReader {
    // utils
    public static String readFileContent(String file) throws Exception {
        String result = "";
        StringBuffer source = new StringBuffer();
        FileInputStream f = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(f);
        String line = null;
        try {
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                source.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = source.toString();

        return result;
    }

    public static String readSource(String name) {
        String result = "";
        StringBuffer source = new StringBuffer();
        InputStream is = SpringMemTransformer.class.getClassLoader().getResourceAsStream(name);

        InputStreamReader isr = new InputStreamReader(is);
        String line = null;
        try {
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                source.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = source.toString();

        return result;
    }
}
