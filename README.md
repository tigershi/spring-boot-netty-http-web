# netty http server with spring boot

  本项目是基于netty4.x 实现的一个httpserver 以及restful风格的mvc框架，适合所有需要提供restful接口的微服务应用, 实现与spring-boot无缝集成，实现了返回参数为Json格式及interceptor， listener， exceptionHandler功能。 

## 优点

* 使用注解配置简单，支持所有restful接口的微服务应用
* 支持HTTPS协议
* 支持使用注解实现路由方法
* 实现与spring-boot无缝集成
* netty使用多路复用技术大幅提升性能
* 减少web容器依赖，减少jar包体积
* 完全按照springmvc的模式开发配置相似，容易上手
* 支持支持各种规则的path配置
* 实现了参数注解和自动注入使用方便

# 快速开始
## 先决条件
-------------

- JDK 8 
- git

## 怎样得到代码及编译
```
git clone git@github.com:tigershi/netty-http-server.git
cd netty-http-server
gradlew build
```
执行完上边的命令就可以看到gradle组织结构netty-http-server项目
* netty-mvc-core项目 netty http sever及mvc核心代码
* netty-mvc-springboot项目, 实现和springboot无缝集成
* netty-mvc-sample项目，使用netty-http-server项目的示例代码：
 包括怎样使用注解实现restful风格接口contoller，怎样使用注解实现interceptor，listener，exceptionHandler


##运行netty-mvc-sample项目
```
运行netty-mvc-sample\src\main\java\io\netty\springboot\sample\TestAppBoot.java 类
就可以运行sample项目
```
在浏览器的地址是：
> https://localhost:8099

具体的如何应用请参考netty-mvc-sample项目

有问题欢迎随时提issues. 

Thanks
