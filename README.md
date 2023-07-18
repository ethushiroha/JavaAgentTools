# JavaAgentTools



参考文章：

-   Java内存马
    -   上：https://mp.weixin.qq.com/s/YVwqD6SwUq_jkEe_9afBCg
    -   下：https://mp.weixin.qq.com/s/gmKSmW5SIME8lWKj8bvhWw



## 2022/11/15
更新了 agent，发现一种特殊情况————使用 systemctl 启动 jar，会导致attach的时候报 `AttachNotSupportedException: Unable to open socket file` 异常。在研究之后解决问题，更新代码。

## 2021.8.31

更新了各个项目中的 Readme