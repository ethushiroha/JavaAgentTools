# Agent

只是一个普通的Java agent，使用方法为 `java -jar Agent.jar pid=<your target pid> file=<your transformer jar file> [path=<target java_pid file>]`

参数说明：
- pid： 目标进程 pid， 必需
- file： 想要注入的 transformer 的 jar 位置，必需
- path： 目标 .java_pid 文件的目录， 默认为 /tmp/， 非必需，仅当目标 .java_pid 文件位置发生变动时需要（例如以 Systemctl 方式启动进程）

当以 systemctl start 方式启动 jar ，且目标开启了 `PrivateTemp` 的时候，其 **.java_pid{pid}** 文件不在 `/tmp` 目录下，而是在 `/tmp/system-xxx-yyy.service-zzz/tmp` 下，导致 agent 无法找到 socks 通信，也就无法 attach 到目标上。
基于此，将 `sun.tool.attach.LinuxVirtualMachine` 重写为 MyLVM 类，需要利用时：
1. 使用时传入参数 path="xxx"

## 参考文章
attach 相关原理可以看：
1. [源码解析 java attach](https://www.cnblogs.com/Jack-Blog/p/15026267.html)
2. [JVM源码分析之Attach机制实现完全解读](https://mp.weixin.qq.com/s?__biz=MzIzNjI1ODc2OA==&mid=2650886799&idx=1&sn=108c5fdfcd2695594d4f80ff02fc9a70&mpshare=1&scene=21&srcid=0114WsKpUmDXhRtqy8x7JX5w#wechat_redirect)
3. [Systemd Unit文件中PrivateTmp字段详解-Jason.Zhi](https://www.cnblogs.com/lihuobao/p/5624071.html)
