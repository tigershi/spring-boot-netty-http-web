package io.netty.mvc.config;

import java.io.Serializable;

import io.netty.handler.ssl.SslContext;
import io.netty.mvc.server.NettyMvcDispatcher;
import io.netty.mvc.server.NettyRestContext;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public class NettyRestConfigures  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3594838354211618550L;
	private int port;
	private int bossGroupSize;
	private int workGroupSize;

	private NettyRestContext context;

	private NettyMvcDispatcher nettyMvcDispatcher;

	private SslContext sslContext=null;


	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public NettyRestContext getContext() {
		return context;
	}

	public void setContext(NettyRestContext context) {
		this.context = context;
	}

	public NettyMvcDispatcher getNettyMvcDispatcher() {
		return nettyMvcDispatcher;
	}

	public void setNettyMvcDispatcher(NettyMvcDispatcher nettyMvcDispatcher) {
		this.nettyMvcDispatcher = nettyMvcDispatcher;
	}

	public SslContext getSslContext() {
		return sslContext;
	}

	public void setSslContext(SslContext sslContext) {
		this.sslContext = sslContext;
	}

	public int getBossGroupSize() {
		return bossGroupSize;
	}

	public void setBossGroupSize(int bossGroupSize) {
		this.bossGroupSize = bossGroupSize;
	}

	public int getWorkGroupSize() {
		return workGroupSize;
	}

	public void setWorkGroupSize(int workGroupSize) {
		this.workGroupSize = workGroupSize;
	}
}
