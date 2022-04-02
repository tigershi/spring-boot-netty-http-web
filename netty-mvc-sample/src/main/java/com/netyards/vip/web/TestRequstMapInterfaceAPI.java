package com.netyards.vip.web;

import io.netty.mvc.annotation.NettyRequestMapping;

/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public interface TestRequstMapInterfaceAPI {
	@NettyRequestMapping(value = "/test")
	public TestModel abc() ;
	
	@NettyRequestMapping(value="/testStr")
	public String abcStr();
}
