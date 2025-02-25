package io.netty.springboot.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
@SpringBootApplication
public class TestAppBoot {
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		//new SpringApplicationBuilder(VIPServerTestApplication.class).web(WebApplicationType.NONE).run(args);
		SpringApplication.run(TestAppBoot.class, args);
		
	}

}
