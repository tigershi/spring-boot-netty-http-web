package io.netty.mvc.server;

import io.netty.mvc.bind.NettyAppListener;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public class NettyListenerWrapper implements Comparable<NettyListenerWrapper>{
	
	 public NettyListenerWrapper( String name, int index, NettyAppListener listener) {
		this.name = name;
		this.index = index;
		this.listener = listener;
	}
      private final String name;
	  private final int index;
	  private final NettyAppListener listener;

	@Override
	public int compareTo(NettyListenerWrapper o) {
		// TODO Auto-generated method stub
		return this.index-o.index;
	}

	public int getIndex() {
		return index;
	}

	public NettyAppListener getListener() {
		return listener;
	}

	public String getName() {
		return name;
	}

}
