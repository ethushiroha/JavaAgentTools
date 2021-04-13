package com.stdout.Utils.Redefine;

import java.io.File;

public class MyMultipartFile {

    public static String getOriginalFilename(Object file) throws Exception {
        return (String) file.getClass().getDeclaredMethod("getOriginalFilename", null).invoke(file, new Object[] {});
    }

    public static void transferTo(Object file, String path) throws Exception {
        File dst = new File(path);
        file.getClass().getDeclaredMethod("transferTo", File.class).invoke(file, dst);
    }

    public static boolean isEmpty(Object file) throws Exception {
        return (boolean) file.getClass().getDeclaredMethod("isEmpty", null).invoke(file, new Object[] {});
    }
}
