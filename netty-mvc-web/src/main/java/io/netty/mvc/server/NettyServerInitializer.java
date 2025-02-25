package io.netty.mvc.server;


import javax.net.ssl.SSLEngine;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.mvc.config.NettyRestConfigures;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

	public NettyServerInitializer(NettyRestConfigures conf, SslContext sslContext) {
		this.config = conf;
		this.sslContext = sslContext;
	}
	private final NettyRestConfigures config;
    private final SslContext sslContext;
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		
		
		ChannelPipeline pipeline = ch.pipeline();

		
		if(sslContext != null) {
			SSLEngine engine = sslContext.newEngine(ch.alloc());
			engine.setUseClientMode(false);
			pipeline.addFirst("ssl", new SslHandler(engine));			
		}
		pipeline.addLast("httpServerRequest", new HttpRequestDecoder());
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		pipeline.addLast("httpServerResponse", new HttpResponseEncoder());
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		//pipeline.addLast("deflater", new HttpContentCompressor());
		pipeline.addLast("handler", new NettyRequestHandler(config));
	}

}
