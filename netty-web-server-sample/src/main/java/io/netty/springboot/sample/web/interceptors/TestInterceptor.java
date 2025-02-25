package io.netty.springboot.sample.web.interceptors;

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
