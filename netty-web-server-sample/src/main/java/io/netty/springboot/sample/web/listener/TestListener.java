package io.netty.springboot.sample.web.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.mvc.annotation.component.NettyListener;
import io.netty.mvc.bind.NettyAppListener;
import io.netty.mvc.config.NettyRestConfigures;


/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
@NettyListener
public class TestListener implements NettyAppListener{
	 private static Logger logger = LoggerFactory.getLogger(TestListener.class);

	@Override
	public void created(NettyRestConfigures configure) {
		// TODO Auto-generated method stub
		 logger.info("this is test the start TestListener");
	}

	@Override
	public void destoryed(NettyRestConfigures configure) {
		// TODO Auto-generated method stub
		 logger.info("this is test the stop TestListener");
	} 
	
}
