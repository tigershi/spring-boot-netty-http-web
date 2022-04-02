package io.netty.mvc.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;

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
		configure.setPort(port);
		configure.setContext(context);
		configure.setNettyMvcDispatcher(new NettyMvcDispatcher(context));
		configure.setSslContext(initSslContext());
		server = new NettyRestServer(configure);
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
		context.initRequestMap(restControllerObj);
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
