package com.netyards.vip.web.exception;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public class TestExceptionWrapper {

		private final int errCode;
	    private String msg;
	   
	    
	    public TestExceptionWrapper(int errCodeNum, String message) {
	    	this.errCode = errCodeNum;
	    	this.msg = message;
	    }
				
	    
	    public TestException vIPException() {
	    	return new TestException(this.msg, this.errCode);
	    }
	    
	    public TestException vIPException(Throwable cause) {
	    	return new TestException( this.errCode, this.msg,  cause);
	    }
	    
	    
	    public String toString() {
	    	return "Error Code["+this.errCode+"] Infomation: "+this.msg +"\n";
	    }
	    
	    
	    public String appendString(String append) {
	    	return "Error Code["+this.errCode+"] Infomation: "+this.msg +"\n"+append;
	    }
	    
	   

}
