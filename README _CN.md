# netty http server with spring boot

  本项目是基于netty4.x 实现的一个httpserver,支持NIO, EPOLL, IO_Uring 以及restful风格的mvc框架，适合所有需要提供restful接口的微服务应用, 实现与spring-boot无缝集成，注解化实现了返回requestbody为Json格式， 支持（requestbody , requestparameter, pathvalue）参数自动注入 , restful-controller, interceptor， listener， exceptionHandler功能。 

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

## 如何使用exceptionHandler
```
@NettyMvcExceptionHandler
public class TestException extends NettyMvcException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -368517557855937761L;

	 private static HttpResponseStatus status = new HttpResponseStatus(512, "bussiness ERROR");
	 private int errorCode = -1;
	 
	public TestException(String message, int errcode) {
		super( message);
		// TODO Auto-generated constructor stub
		this.setErrorCode(errcode);
	}

	public TestException(int errcode, String message,  Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
		this.setErrorCode(errcode);
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Error Code: [" +getErrorCode()+ "] Error Message: " + super.getMessage();
	}

	@Override
	public String getExceptionJsonMsg() {
		// TODO Auto-generated method stub
StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		sb.append("\"errCode\":");
		sb.append(getErrorCode());
		sb.append(", \"message\":");
		sb.append("\"");
		sb.append("[Netty Test Server] ");
		sb.append(super.getMessage());
		sb.append("\"");
		sb.append(", \"timestamp\":");
		sb.append(System.currentTimeMillis());
		sb.append("}");
		
		return sb.toString();

	}

	@Override
	public HttpResponseStatus getResponseStatus() {
		// TODO Auto-generated method stub
		return status;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
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
