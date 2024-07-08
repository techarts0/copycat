# copycat

#### 介绍
Copycat是一个完全自研发并且开源的网络应用开发框架，与大名鼎鼎的Netty类似。它具有高性能、简单易用的特点，为Java平台的网络应用开发者提供了一种开箱即用的、理想的选择。<br>
互联网时代，几乎所有软件都需要通过网络进行通信，构成分布式应用架构，系统的复杂度和规模空前。然而网络通信有两大难点：网络编程和协议处理。一些应用开发者(尤其是Java、Python等非系统编程语言)还不熟悉TCP/IP协议栈、Socket编程模型、高并发、协议设计和解析等知识和技能。Copycat屏蔽了网络编程的底层细节，旨在为开发者提供了一套简约的、面向协议的编程模型，任何人都能开发出高性能、可靠的网络应用，正如同我们的slogan：让网络编程跟撸猫一样简单。<br>
与Netty等同类型项目相比，Copycat最大的、独特的优势是：为物联网应用场景做了大量的优化和简化。它不仅实现了MQTT、CoAP两个通用物联网应用协议，还实现(或规划)了针对智能楼宇(bacnet)、智慧能源(dlt645/mbus)、智能电网(iec104)、工业控制(modbus)等行业标准协议，并提供了一种通用的非标透传协议(mote)，让物联网应用开发者远离繁琐技术，专注于业务，更快速地开发应用。<br>
Copycat具有设计良好的架构，采用了Java平台若干新技术（异步IO、虚拟线程），并且优化内存模型实现Zero-Copy，这是它能实现高性能的基本保证。作为一个开源项目，我们希望有更多的开发者和爱好者参与进来，通过提Issue、贡献代码、项目应用等方式让Copycat成为一个活跃的、优秀的开源项目，为更多的开发者提供便利。

#### 软件架构
Copycat整体架构如下：<br>
![输入图片说明](arch.png)
<br>Copycat对物联网应用的支持计划如下：<br>
![输入图片说明](iot.png)


#### 安装教程

1.  从源代码编译<br>
 在本地新建一个Maven项目(Eclipse或IDEA都可以)，然后下载源代码并拷贝到src目录中。除了junit之外，Copycat没有任何依赖，因此pom.xml可以用您的IDE生成的。
 请注意：JDK版本必须21或以上，否则不支持Virtual Thead，无法通过编译。当然您也可以注释掉相关的几行代码。
2.  直接引用JAR<br>
 当然，直接下载编译好的copycat.jar更简单，加入到您的项目的classpath中。目前没有提交到maven中央仓库中，所以您无法直接在pom.xml中引用。

#### 使用说明

1.  如何使用Copycat<br>
 太简单了也没什么好说的，请看源代码中cn.techarts.copycat.demo下的小例子。五分钟还没学会怎用，您找我，赔您一分钱。
2.  注意事项<br>
 Context是服务器端的配置信息。如果您是在低版本JDK中使用，请务必注意，不能启用虚拟线程，即不用调用方法enableVirtualThread。
 

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)