package io.netty.mvc.server;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.mvc.config.NettyRestConfigures;
import io.netty.util.CharsetUtil;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
@Sharable
public class NettyRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
	
	private static Logger logger = LoggerFactory.getLogger(NettyRestServer.class); 

	public NettyRequestHandler(NettyRestConfigures conf) {
		this.config = conf;
	}

	private NettyRestConfigures config;
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
    	  logger.error(e.getMessage(),e);
            sendError(ctx, INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status, String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        response.headers().set("Content-Type", "text/plain; charset=UTF-8");
        ByteBuf byteBuf = Unpooled.copiedBuffer(msg,CharsetUtil.UTF_8);
		 response.content().writeBytes(byteBuf);
		 byteBuf.release();
        // Close the connection as soon as the error message is sent.
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    }
	
	

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
		// TODO Auto-generated method stub
	/*	  if (fullHttpRequest.uri().equals("/favicon.ico")) {
              return;
          }*/
		  if (!fullHttpRequest.decoderResult().isSuccess()) {
	            sendError(ctx, BAD_REQUEST, "Bad Request");
	            return;
	        }
		  
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
       try {
    	   config.getNettyMvcDispatcher().service(fullHttpRequest, response);
    	   
       }catch(Exception e){
    		   response.headers().set("Content-Type", "text/plain; charset=UTF-8");
    		   String resultErrStr = (e.getMessage()==null)? INTERNAL_SERVER_ERROR.reasonPhrase(): e.getMessage();
    		   ByteBuf byteBuf = Unpooled.copiedBuffer(resultErrStr,CharsetUtil.UTF_8);
    		   response.content().writeBytes(byteBuf);
    		   response.setStatus(INTERNAL_SERVER_ERROR);
    		   byteBuf.release();
    	      logger.error(resultErrStr, e);
    	   
       }
       
       
       response.headers().set("Access-Control-Allow-Origin", "*");
       response.headers().set("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With,X-File-Name");
       response.headers().set("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
       response.headers().set("Content-Length", Integer.valueOf(response.content().readableBytes()));
       response.headers().set("Connection", "keep-alive");
       //logger.info("resultContent--------"+response.content());
       ChannelFuture writeFuture = ctx.writeAndFlush(response);
       writeFuture.addListener(ChannelFutureListener.CLOSE);
		
	}

}
