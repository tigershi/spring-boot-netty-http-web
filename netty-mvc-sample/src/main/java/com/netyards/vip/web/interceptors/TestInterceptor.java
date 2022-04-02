package com.netyards.vip.web.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.mvc.annotation.component.NettyInterceptor;
import io.netty.mvc.bind.NettyHttpRequest;
import io.netty.mvc.bind.NettyHttpResponse;
import io.netty.mvc.bind.NettyMvcInterceptor;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
@NettyInterceptor
public class TestInterceptor implements NettyMvcInterceptor{
	 private static Logger logger = LoggerFactory.getLogger(TestInterceptor.class); 
	 public boolean preHandle(NettyHttpRequest  request, NettyHttpRequest response) throws Exception { 
		 
		 
		 logger.info("this is test the preHander interceptor");
		 
		 return true;
		 };

	@Override
	public void postHandle(NettyHttpRequest request, NettyHttpResponse response, Object responseObj) throws Exception {
		// TODO Auto-generated method stub
		logger.info("this is test the postHander interceptor");
	}

}
