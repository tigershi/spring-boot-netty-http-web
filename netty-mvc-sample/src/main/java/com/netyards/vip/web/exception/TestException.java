
package com.netyards.vip.web.exception;



import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.mvc.annotation.NettyMvcExceptionHandler;
import io.netty.mvc.bind.NettyMvcException;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
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
