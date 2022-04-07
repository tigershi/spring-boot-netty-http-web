# Netty http server with spring-boot

  netty-http-server is base on netty4.x. It can provide restful style MVC framework，use as mirco-server and integrate with spring-boot seamlessly. It also can annotate the implementation (response json body,
  requestbody , requestparameter, pathvalue parameter auto-inject), interceptor, restful-controller, listener and exceptionHandler function

## advantages

* annotate restful style MVC framework and use as mirco-serverN
* support HTTPS
* support use @NettyRestController as controller
* integrate with spring-boot seamlessly
* netty have a advantage in profermance
* reduce web container and jar dependency
* similar with springmvc，easy to learn
* support path value configure use annotation
* support parameter annotation and parameter auto-inject

# quick start

## prequirement
-------------

- JDK 8 
- git

## how to get code and build
```
git clone git@github.com:tigershi/netty-http-server.git
cd netty-http-server
gradlew build
```

## The netty-http-server structure
 
* netty-mvc-core:  netty http sever and mvc core code
* netty-mvc-springboot: springboot integration code
* netty-mvc-sample: netty-http-server Sample code project(include: how to use response json body,
  requestbody , requestparameter, pathvalue, interceptor, restful-controller, listener and exceptionHandler)


## netty-http-server restfulcontroller
### base on java class' annotation
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

### base on interface's annotation
    It usually use in mirco-service server

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


## netty-http-server interceptor

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


## how to use netty-http-server listener
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

## how to use netty-http-server exceptionHandler
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





##run netty-mvc-sample 

  the class netty-mvc-sample\src\main\java\io\netty\springboot\sample\TestAppBoot.java 
is the netty-mvc-sample project's entrance


The browser address：
> https://localhost:8099

 The detail look on netty-mvc-sample project

 It's my pleasure it if you can provider issue

Thanks
