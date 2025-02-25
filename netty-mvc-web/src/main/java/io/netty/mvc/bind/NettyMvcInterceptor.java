package io.netty.mvc.bind;

/**
 * 
 * @author shihu
 * @Date  2022.04.02
 *
 */
public interface NettyMvcInterceptor {
	
	 /**
	  * custom intercept path  before the execution of current Interceptor
	  * @param formatPath
	  * @param request
	  * @return
	  */
	 public boolean isIterceptPath(String formatPath, NettyHttpRequest  request) ;
	
	   /**
		 * Interception point before the execution of a handler. Called after
		 * HandlerMapping determined an appropriate handler object, but before
		 * HandlerAdapter invokes the handler.
		 * <p>DispatcherServlet processes a handler in an execution chain, consisting
		 * of any number of interceptors, with the handler itself at the end.
		 * With this method, each interceptor can decide to abort the execution chain,
		 * typically sending an HTTP error or writing a custom response.
	     **/	 
	 
	 public boolean preHandle(NettyHttpRequest  request, NettyHttpResponse response) throws Exception;

	   /**
		 * Interception point after successful execution of a handler.
		 * Called after HandlerAdapter actually invoked the handler,
		 * 
		 **/
	
	 public void postHandle(NettyHttpRequest  request, NettyHttpResponse response, Object responseObj) throws Exception;

	   /**
		 * Callback after completion of request processing, that is, after rendering
		 * the view. Will be called on any outcome of handler execution, thus allows
		 * for proper resource cleanup.
		 */
	 public void afterCompletion(NettyHttpRequest request, NettyHttpResponse response, Object responseObj,
				 Exception ex) throws Exception;

}
