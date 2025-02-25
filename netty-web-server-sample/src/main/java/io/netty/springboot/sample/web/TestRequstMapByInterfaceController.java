package io.netty.springboot.sample.web;

import io.netty.mvc.annotation.component.NettyRestController;

/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
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
