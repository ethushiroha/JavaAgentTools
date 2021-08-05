package com.stdout.Models;

import com.stdout.Config.DefaultConfig;
import com.stdout.Utils.Redefine.MyReader;

public class Helper {
    public static String help() {
        String result = MyReader.readSource(DefaultConfig.TemplatesConfig.Help);
        return result;
    }
}
