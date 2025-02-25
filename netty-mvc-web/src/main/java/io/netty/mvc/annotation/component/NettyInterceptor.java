package io.netty.mvc.annotation.component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface NettyInterceptor{
	String value() default "";
	int  sort() default Integer.MAX_VALUE;
}
