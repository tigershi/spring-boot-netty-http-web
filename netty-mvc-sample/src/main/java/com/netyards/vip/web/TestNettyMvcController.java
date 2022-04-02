package com.netyards.vip.web;


import com.netyards.vip.web.exception.TestException;
import com.netyards.vip.web.exception.TestModelExceptionHandlers;

import io.netty.mvc.annotation.NettyPathVal;
import io.netty.mvc.annotation.NettyReqParam;
import io.netty.mvc.annotation.NettyRequestBody;
import io.netty.mvc.annotation.NettyRequestMapping;
import io.netty.mvc.annotation.component.NettyRestController;
import io.netty.mvc.bind.NettyHttpMethods;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
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
