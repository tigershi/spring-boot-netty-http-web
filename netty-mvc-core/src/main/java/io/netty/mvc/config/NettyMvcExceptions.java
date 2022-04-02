package io.netty.mvc.config;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.mvc.bind.NettyMvcException;

public class NettyMvcExceptions {
	private static String formatErrMsg(String errMsg) {
		return "{\"errMsg\" : "+"\""+errMsg+"\"}";
	}
	
	public final static NettyMvcException URL_PARAM_ERR = new NettyMvcException("Incorrect parameter") {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7182911362960929834L;

		@Override
		public HttpResponseStatus getResponseStatus() {
			// TODO Auto-generated method stub
			return HttpResponseStatus.BAD_REQUEST;
		}

		@Override
		public String getExceptionJsonMsg() {
			// TODO Auto-generated method stub
			return formatErrMsg(this.getMessage());
		}};
	
		
		public final static NettyMvcException NOT_FOUND_SOURCE = new NettyMvcException("Not found the resouce") {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7182911362960929834L;

			@Override
			public HttpResponseStatus getResponseStatus() {
				// TODO Auto-generated method stub
				return HttpResponseStatus.NOT_FOUND;
			}

			@Override
			public String getExceptionJsonMsg() {
				// TODO Auto-generated method stub
				return formatErrMsg(this.getMessage());
			}};
		
			public final static NettyMvcException PARAM_NOT_NULL= new NettyMvcException("Parameter is requried, not allow null") {

				/**
				 * 
				 */
				private static final long serialVersionUID = -7182911362960929834L;

				@Override
				public HttpResponseStatus getResponseStatus() {
					// TODO Auto-generated method stub
					return HttpResponseStatus.BAD_REQUEST;
				}

				@Override
				public String getExceptionJsonMsg() {
					// TODO Auto-generated method stub
					return formatErrMsg(this.getMessage());
				}};
			
		
		

}
