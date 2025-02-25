package io.netty.mvc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.mvc.config.NettyRestConfigures;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public class NettyRestServer {
	
	 private static Logger logger = LoggerFactory.getLogger(NettyRestServer.class); 
	
	   public NettyRestServer(NettyRestConfigures conf) {
		   this.config = conf;
	   }
	
	   private NettyRestConfigures config;
	
	   private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	   private final EventLoopGroup workerGroup = new NioEventLoopGroup();

		public void start()  {
			// TODO Auto-generated method stub
			
			for(NettyListenerWrapper wrapper : config.getContext().getListeners()) {
				logger.info("execute started listener---"+ wrapper.getName());
				wrapper.getListener().created(config);
			}
			
			
			ServerBootstrap bootstrap = new ServerBootstrap();
	        EventLoopGroup bossGroup = new NioEventLoopGroup();
	        EventLoopGroup workerGroup = new NioEventLoopGroup();

	        bootstrap.group(bossGroup, workerGroup);
	        bootstrap.channel(NioServerSocketChannel.class);
	        bootstrap.childOption(NioChannelOption.TCP_NODELAY, true);
	        bootstrap.childOption(NioChannelOption.SO_REUSEADDR,true);
	        bootstrap.childOption(NioChannelOption.SO_KEEPALIVE,true);
	        
	      //  bootstrap.childOption(NioChannelOption.SO_BACKLOG, 1024);
	        bootstrap.childHandler(new NettyServerInitializer(config, config.getSslContext()));
	        
	    	/*	try {
			ChannelFuture f = bootstrap.bind(port).sync();
			// 等待服务器 socket 关闭 。在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    
	        
	       ChannelFuture channelFuture = bootstrap.bind(config.getPort()).syncUninterruptibly().addListener(future -> {
	            String logBanner = "\n\n" +
	                    "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
	                    "*                                                                                   *\n" +
	                    "*                                                                                   *\n" +
	                    "*                   Netty Http Server started on port {}.                         *\n" +
	                    "*                                                                                   *\n" +
	                    "*                                                                                   *\n" +
	                    "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n";
	            logger.info(logBanner, config.getPort());
	        });
	        channelFuture.channel().closeFuture().addListener(future -> {
	        	for(NettyListenerWrapper wrapper : config.getContext().getListeners()) {
	        		logger.info("execute stoped listener---"+ wrapper.getName());
					wrapper.getListener().destoryed(config);
				}
	        	logger.info("Netty Http Server Start Shutdown ............");
	            bossGroup.shutdownGracefully();
	            workerGroup.shutdownGracefully();
	        });
		}



		public void stop() {
			// TODO Auto-generated method stub
			 bossGroup.shutdownGracefully();
	         workerGroup.shutdownGracefully();
		}

}
