package io.netty.mvc.bind;

/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public interface NettyMvcInterceptor {
	
	 default  boolean preHandle(NettyHttpRequest  request, NettyHttpResponse response)throws Exception { return true;};

	/**
	 * This implementation is empty.
	 */
	
	 public void postHandle(NettyHttpRequest  request, NettyHttpResponse response, Object responseObj) throws Exception;



}
