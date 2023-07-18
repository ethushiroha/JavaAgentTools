# Agent

只是一个普通的Java agent，使用方法为 `java -jar Agent.jar [pid] [jarFile]`

当以 systemctl start 方式启动 jar ，且目标开启了 `PrivateTemp` 的时候，其 **.java_pid{pid}** 文件不在 `/tmp` 目录下，而是在 `/tmp/system-xxx-yyy.service-zzz/tmp` 下，导致 agent 无法找到 socks 通信，也就无法 attach 到目标上。
基于此，将 `sun.tool.attach.LinuxVirtualMachine` 重写为 MyLVM 类，需要利用时：
1. 将 sun.tools.attach.MyLVM 中的 `public static String tmpdir = "/tmp/";` 更改为目标 `.java_pid{pid}` 所在的=目录
2. 修改 `AgentMain.java` ， 使用 `UseMyLVM(id, jarName)` 函数，注释掉另一个
```java
        UseMyLVM(id, jarName);
//        UseNativeVM(id, jarName);
```
3. 编译，注入

## 参考文章
attach 相关原理可以看：
1. [源码解析 java attach](https://www.cnblogs.com/Jack-Blog/p/15026267.html)
2. [JVM源码分析之Attach机制实现完全解读](https://mp.weixin.qq.com/s?__biz=MzIzNjI1ODc2OA==&mid=2650886799&idx=1&sn=108c5fdfcd2695594d4f80ff02fc9a70&mpshare=1&scene=21&srcid=0114WsKpUmDXhRtqy8x7JX5w#wechat_redirect)
3. [Systemd Unit文件中PrivateTmp字段详解-Jason.Zhi](https://www.cnblogs.com/lihuobao/p/5624071.html)
