package io.netty.mvc.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;

import io.netty.channel.epoll.Epoll;
import io.netty.incubator.channel.uring.IOUring;
import io.netty.mvc.config.Constants;
import io.netty.util.NettyRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.mvc.annotation.component.NettyInterceptor;
import io.netty.mvc.annotation.component.NettyListener;
import io.netty.mvc.annotation.component.NettyRestController;
import io.netty.mvc.bind.NettyMvcException;
import io.netty.mvc.config.NettyConfigConstant;
import io.netty.mvc.config.NettyRestConfigures;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
@Component
public class NettyServerSpringBootWrapper implements CommandLineRunner, ApplicationListener<ContextRefreshedEvent> {

	private static Logger logger = LoggerFactory.getLogger(NettyServerSpringBootWrapper.class);

	@Autowired
	private Environment environment;

	private NettyRestContext context;
	private NettyRestConfigures configure;
	private NettyRestServer server;
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		logger.info("start command line");
		configure = new NettyRestConfigures();
		Integer port = environment.getProperty(NettyConfigConstant.PORT, Integer.class);
		//environment.getProperty(NettyConfigConstant, Integer.class)
		Integer bossSize = environment.getProperty(NettyConfigConstant.BOSS_GROUP_SIZE, Integer.class);
		Integer workSize = environment.getProperty(NettyConfigConstant.WORK_GROUP_SIZE, Integer.class);
         if (bossSize == null  || bossSize == 0){
			 configure.setBossGroupSize(Constants.SERVER_CONFIG_BOSS_GROUP_DEFAULT_SIZE);
		 }else {
			 configure.setBossGroupSize(bossSize);
		 }

		 if (workSize == null){
			 configure.setWorkGroupSize(Constants.SERVER_CONFIG_WORK_GROUP_DEFAULT_SIZE);
		 }else {
			 configure.setWorkGroupSize(workSize);
		 }

		configure.setPort(port);
		configure.setContext(context);
		configure.setNettyMvcDispatcher(new NettyMvcDispatcher(context));
		configure.setSslContext(initSslContext());
		String startType = environment.getProperty(NettyConfigConstant.EVENT_GROUP_TYPE, String.class);

		if (startType == null || startType.isEmpty() || startType.equalsIgnoreCase(Constants.SERVER_CONFIG_GROUP_DEFAULT_TYPE)){
			if(Epoll.isAvailable()){
				server = new NettyEpollRestServer(configure);
			}else{
				server = new NettyNioRestServer(configure);
			}
		}else if(Constants.SERVER_CONFIG_GROUP_EPOLL_TYPE.equalsIgnoreCase(startType) && Epoll.isAvailable()){
			server = new NettyEpollRestServer(configure);
		}else if(Constants.SERVER_CONFIG_GROUP_IO_URING_TYPE.equalsIgnoreCase(startType) && IOUring.isAvailable()){
		    server = new NettyIoUringRestServer(configure);
	     }else {
			server = new NettyNioRestServer(configure);
		}

		System.gc();
		server.start();

	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		context = new NettyRestContext();
		Map<String, Object> restControllerObj = event.getApplicationContext()
				.getBeansWithAnnotation(NettyRestController.class);
		logger.info("the netty controller size-" + restControllerObj.size());
		try {
			context.initRequestMap(restControllerObj);
		} catch (NettyMvcException e) {
			logger.error(e.getMessage(), e);
			System.exit(1);
		}
		Map<String, Object> nettyInteceptors = event.getApplicationContext()
				.getBeansWithAnnotation(NettyInterceptor.class);
		context.initNettyMvcInterceptorWrapper(nettyInteceptors);

		Map<String, Object> nettyLiteners = event.getApplicationContext().getBeansWithAnnotation(NettyListener.class);
		context.initNettyListenerWrapper(nettyLiteners);
	}

	private SslContext initSslContext() throws  Exception {
		String enableSsl = environment.getProperty(NettyConfigConstant.HTTPS_ENABLE, "false");
		if(Boolean.parseBoolean(enableSsl)) {
			String keyStorePath = environment.getProperty(NettyConfigConstant.SSL_KEY_STORE);
			String keyStorePasswd = environment.getProperty(NettyConfigConstant.SSL_KEY_STORE_PASSWORD);
			String keyStoreType = environment.getProperty(NettyConfigConstant.SSL_KEY_STORE_TYPE);
			String keyPasswd = environment.getProperty(NettyConfigConstant.SSL_KEY_PASSWORD);

			KeyManagerFactory keyManagerFactory = null;
			KeyStore ks = KeyStore.getInstance(keyStoreType);

			InputStream inputStream = null;
			if (keyStorePath.toLowerCase().contains("classpath")) {
				int index = keyStorePath.indexOf(":") + 1;
				String path = keyStorePath.substring(index);
				inputStream = NettyServerSpringBootWrapper.class.getClassLoader().getResourceAsStream(path);
			} else {
				inputStream = new FileInputStream(keyStorePath);

			}
			ks.load(inputStream, keyStorePasswd.toCharArray());
			keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			keyManagerFactory.init(ks, keyPasswd.toCharArray());
			return SslContextBuilder.forServer(keyManagerFactory).build();
		}else {
			return null;
		}
	
	}
}
