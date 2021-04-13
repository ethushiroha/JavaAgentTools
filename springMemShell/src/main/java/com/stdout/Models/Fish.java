package com.stdout.Models;

import com.stdout.Utils.Redefine.MyReader;

public class Fish {
    public static boolean isWantFish = false;
    public static String fishUri = "";
    public static String fishContent = "";

    public static String StaticFish(String uri) {
        String result = "";
        if (isWantFish) {
            if (uri.equals(fishUri)) {
                result = fishContent;
            }
        }
        return result;
    }

    public static String fishStart(String target, String file) throws Exception {
        String result = "";
        isWantFish = true;
        fishContent = MyReader.readFileContent(file);
        fishUri = target;
        result = "uri ==> \n\t" + target + "\ncontent ==> \n" + fishContent;
        return result;
    }

    public static String fishStop() {
        isWantFish = false;
        String result = "fish stopped!!";
        return result;
    }

    public static String fishShow() {
        String result = "";

        if (isWantFish) {
            result += "status ==> \n\t" + "started\n\n";
            result += "static path ==> \n\t" + fishUri + "\n\n";
            result += "content ==> \n" + fishContent + "\n\n";
        } else {
            result += "status ==> " + "stopped\n\n";
        }
        return result;
    }
}
