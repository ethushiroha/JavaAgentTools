# AgentWithShell

将 springMemShell 和 Agent 放在一个项目里面，注入的时候不需要带 [jarFile] 参数

使用方法 `java -jar AgentWithShell.jar pid=<your target pid> [path=<your java_pid path>]`

参数说明：
- pid：目标进程的 pid， 必需
- path： 目标进程 .java_pid 文件的位置，默认为 /tmp/， 非必需，仅当目标 .java_pid 文件发生位置变动时才需要传入（例如以 systemctl 方式启动 jar）

Systemctl 方式启动的目标，[利用方法同 Agent](../Agent/README.md)
SpringMemShell 的功能，[参见](../springMemShell/README.md)

