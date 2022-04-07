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
![image](https://user-images.githubusercontent.com/2288615/162167600-0724bb0b-dfe2-41f4-8524-0d3ae0323546.png)

* netty-mvc-core项目 netty http sever及mvc核心代码
* netty-mvc-springboot项目, 实现和springboot无缝集成
* netty-mvc-sample项目，使用netty-http-server项目的示例代码：
 包括怎样使用注解实现restful风格接口contoller，怎样使用注解实现interceptor，listener，exceptionHandler
* publish 文件夹： 编译完发布netty-mvc-core-xxx.jar 和 netty-mvc-springboot-xxx.jar
 ![image](https://user-images.githubusercontent.com/2288615/162168778-f4ec130c-9266-4bd7-b8a2-1db84a59b60a.png)

## 添加netty-http-servic到你的项目
 ### 添加 netty-mvc-core-xxx.jar 和 netty-mvc-springboot-xxx.jar 到你项目的lib文件夹
 ![image](https://user-images.githubusercontent.com/2288615/162169750-b9660665-b0c6-4a69-a289-09cb4bbf2465.png)
 ### 添加依赖到classpath
 gradle
```
 implementation fileTree(dir: 'lib', include: [ '*.jar'])
```
maven
```
<dependency>
     <groupId>io.netty.mvc</groupId>
      <artifactId>netty-mvc-core</artifactId>
      <version>xxx</version>
      <scope>system</scope>
       <systemPath>${project.basedir}/src/main/resources/lib/netty-mvc-core-xxx.jar</systemPath>
</dependency>

<dependency>
     <groupId>io.netty.mvc</groupId>
      <artifactId>netty-mvc-springboot</artifactId>
      <version>xxx</version>
      <scope>system</scope>
       <systemPath>${project.basedir}/src/main/resources/lib/netty-mvc-springboot-xxx.jar</systemPath>
</dependency>

```


## 怎样使用restfulcontroller注解
### 基于java class的注解
```
@NettyRestController
public class TestNettyMvcController {

	@NettyRequestMapping("/testErr")
	public String testError() throws TestException{
		throw TestModelExceptionHandlers.TEST_ERROR.vIPException();
		
		//return "test error";
	}
	
	@NettyRequestMapping(value = "/testmodel", method=NettyHttpMethods.POST)
	public TestModel abc( @NettyRequestBody TestModel test) {
		
		test.setName("this is a test response");
		test.setTest("test return is object");
		return test;
	}
	
	
	@NettyRequestMapping(value = "/testpathval/{name}/{age}", method=NettyHttpMethods.POST)
	public TestModel abc( @NettyPathVal(name="name") String name, @NettyPathVal(name="age")Integer age,
			@NettyReqParam(name = "sex") Boolean sex,
			@NettyReqParam(name = "phone") String phone,
			@NettyReqParam(name = "grade") String grade
			) {
		TestModel test = new TestModel();
		test.setName(name + age + phone+String.valueOf(sex));
		test.setTest("test return is object+"+grade);
		return test;
	}
}
```

### 基于interface的注解
    此方式比较适合微服务开发

```
public interface TestRequstMapInterfaceAPI {
	@NettyRequestMapping(value = "/test")
	public TestModel abc() ;
	
	@NettyRequestMapping(value="/testStr")
	public String abcStr();
}

@NettyRestController
public class TestRequstMapByInterfaceController implements TestRequstMapInterfaceAPI {


public TestModel abc() {
	TestModel test = new TestModel();
	test.setId(23);
	test.setName("this is a test");
	test.setTest("test return is object");
	return test;
}


public String abcStr() {
	return "this is a test";
}

}

```


## 如何使用interceptor

```
@NettyInterceptor
public class TestInterceptor implements NettyMvcInterceptor{
	
	 private static Logger logger = LoggerFactory.getLogger(TestInterceptor.class); 
	@Override
	public void postHandle(NettyHttpRequest request, NettyHttpResponse response, Object responseObj) throws Exception {
		// TODO Auto-generated method stub
		logger.info("this is test the postHander interceptor");
	}

	@Override
	public boolean isIterceptPath(String formatPath, NettyHttpRequest request) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void afterCompletion(NettyHttpRequest request, NettyHttpResponse response, Object responseObj, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		logger.info("this is test the afterCompletion interceptor");
		
	}

	@Override
	public boolean preHandle(NettyHttpRequest request, NettyHttpResponse response) throws Exception {
		// TODO Auto-generated method stub
		logger.info("this is test the preHander interceptor");
		return true;
	}

}
```


## 如何使用listener
```
@NettyListener
public class TestListener implements NettyAppListener{
	 private static Logger logger = LoggerFactory.getLogger(TestListener.class);

	@Override
	public void created(NettyRestConfigures configure) {
		// TODO Auto-generated method stub
		 logger.info("this is test the start TestListener");
	}

	@Override
	public void destoryed(NettyRestConfigures configure) {
		// TODO Auto-generated method stub
		 logger.info("this is test the stop TestListener");
	} 
	
}
```






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
