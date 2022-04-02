package io.netty.mvc.server;

import io.netty.mvc.bind.NettyMvcInterceptor;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public class NettyMvcInterceptorWrapper implements Comparable<NettyMvcInterceptorWrapper>{
	


	private final int index;
	private final NettyMvcInterceptor intercepter;
	

	public NettyMvcInterceptorWrapper(int index, NettyMvcInterceptor intercepter) {
		this.index = index;
		this.intercepter = intercepter;
	}


	@Override
	public int compareTo(NettyMvcInterceptorWrapper o) {
		// TODO Auto-generated method stub
		return this.index-o.index;
	}


	public int getIndex() {
		return index;
	}


	public NettyMvcInterceptor getIntercepter() {
		return intercepter;
	}
	
	

}
