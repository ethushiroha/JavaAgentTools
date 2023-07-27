# AgentWithShell

将 springMemShell 和 Agent 放在一个项目里面，注入的时候不需要带 [jarFile] 参数

使用方法 `java -jar AgentWithShell.jar pid=<your target pid> [path=<your java_pid path>]`

参数说明：
- pid：目标进程的 pid， 必需
- path： 目标进程 .java_pid 文件的位置，默认为 /tmp/， 非必需，仅当目标 .java_pid 文件发生位置变动时才需要传入（例如以 systemctl 方式启动 jar）

Systemctl 方式启动的目标，[利用方法同 Agent](../Agent/README.md)
SpringMemShell 的功能，[参见](../springMemShell/README.md)



欢迎各位师傅提出建议，提出需要添加的功能，我尽量做（在做了在做了



## 20230727 update

添加了 Suo5 的代理，路由在 `Config.DefaultConfig.ProxyConfig.Suo5Proxy` 

默认为 `/VTNWdk5Rbz0K` 

![image-20230727165000623](https://s2.loli.net/2023/07/27/QmYDSqpFuEGJMrx.png)

实现在 `Models.Suo5` 里面
