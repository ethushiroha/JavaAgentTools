package com.stdout.springMem;

import java.io.*;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;

public class SpringMemModels {

    // models
    public static String exit() throws UnmodifiableClassException {
        return SpringMemShell.back();
    }

    public static String help() {
        String result = "";
        result += "models: \n";

        result += "\t 1. exec ==> execute system command\n";
        result += "\t\t param: cmd\n\n";

        result += "\t 2. exit ==> remove the SpringMemShell\n";
        result += "\t\t param: \n\n";

        result += "\t 3. fish ==> static fish\n";
        result += "\t\t action ==> start";
        result += "\t\t\t param: target file\n\n";
        result += "\t\t action ==> stop\n";
        result += "\t\t\t param: \n\n";
        result += "\t\t show ==> show the fish info\n";
        result += "\t\t\t param: \n\n";

        result += "\t 4. proxy ==> reGeorg proxy\n";
        result += "\t\t param: \n\n";


        return result;
    }

}
