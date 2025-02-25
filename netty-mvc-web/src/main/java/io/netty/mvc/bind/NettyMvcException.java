package io.netty.mvc.bind;

import io.netty.handler.codec.http.HttpResponseStatus;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public abstract class NettyMvcException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3912856405232240196L;


	public NettyMvcException(){
		super();
	}
	public NettyMvcException(String message) {
		super(message);
		
	}

	public NettyMvcException(String message, Throwable cause) {
		super(message, cause);

	}

	public NettyMvcException(Throwable cause) {
		super(cause);

	}
 
	protected NettyMvcException(String message, Throwable cause,
             boolean enableSuppression,
             boolean writableStackTrace) {
	 super(message, cause, enableSuppression, writableStackTrace);
 }

	public abstract HttpResponseStatus getResponseStatus();

	public  abstract String getExceptionJsonMsg();

}
