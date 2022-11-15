# ShiroAgent

只是一个普通的Java agent

使用方法在release 中有写

## 2022/11/15 update
当以 systemctl start 方式启动 jar 的时候，其 **.java_pid{pid}** 文件不在 `/tmp` 目录下，而是在 `/tmp/system-xxxx-xxx.service/tmp` 下，导致 agent 无法找到 socks 通信，也就无法 attach 到目标上，基于将 `sun.tool.attach.LinuxVirtualMachine` 重写为 MyLVM 类，需要使用时，修改 MyLVM.tmpdir 即可。

attach 相关原理可以看：
1. [源码解析 java attach](https://www.cnblogs.com/Jack-Blog/p/15026267.html)
2. [JVM源码分析之Attach机制实现完全解读](https://mp.weixin.qq.com/s?__biz=MzIzNjI1ODc2OA==&mid=2650886799&idx=1&sn=108c5fdfcd2695594d4f80ff02fc9a70&mpshare=1&scene=21&srcid=0114WsKpUmDXhRtqy8x7JX5w#wechat_redirect)

该使用场景比较少，所以 release 就不改了，有需要可以自行编译，编译需要将 pom.xml 中的 tools.jar 的路径换成本地jdk中 tools.jar 的路径。
