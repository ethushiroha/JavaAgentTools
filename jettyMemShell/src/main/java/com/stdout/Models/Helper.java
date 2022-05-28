package com.stdout.Models;

import com.stdout.Config.ShellConfig;
import com.stdout.Utils.Redefine.MyReader;

public class Helper {
    public static String help() {
        return MyReader.readSource(ShellConfig.TemplatesConfig.Help);
    }
}
