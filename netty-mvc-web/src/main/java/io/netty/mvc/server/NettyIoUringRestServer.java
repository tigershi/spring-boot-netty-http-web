package io.netty.mvc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.incubator.channel.uring.IOUringChannelOption;
import io.netty.incubator.channel.uring.IOUringEventLoopGroup;
import io.netty.incubator.channel.uring.IOUringServerSocketChannel;
import io.netty.mvc.config.NettyRestConfigures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyIoUringRestServer implements NettyRestServer{
    private static Logger logger = LoggerFactory.getLogger(NettyIoUringRestServer.class);

    private NettyRestConfigures config;

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    public NettyIoUringRestServer(NettyRestConfigures conf) {

        this.config = conf;
        bossGroup = new IOUringEventLoopGroup(this.config.getBossGroupSize());
        workerGroup = new IOUringEventLoopGroup(this.config.getWorkGroupSize());

    }



    public void start()  {
        // TODO Auto-generated method stub

        for(NettyListenerWrapper wrapper : config.getContext().getListeners()) {
            logger.info("execute started listener---"+ wrapper.getName());
            wrapper.getListener().created(config);
        }


        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(IOUringServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_REUSEADDR,true);
        bootstrap.option(IOUringChannelOption.SO_REUSEPORT, true);
        bootstrap.childOption(IOUringChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(IOUringChannelOption.SO_KEEPALIVE,true);

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
                    "*                   Netty IO_Uring Http Server started on port {}.                         *\n" +
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
