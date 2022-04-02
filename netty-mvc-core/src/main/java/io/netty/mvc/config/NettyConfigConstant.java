package io.netty.mvc.config;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public interface NettyConfigConstant {

	public static String PORT=Constants.SERVER_CONFIG_PREFIX+"port";
	public static String HTTPS_ENABLE=Constants.SERVER_CONFIG_PREFIX+"ssl.enable";
	public static String SSL_KEY_STORE = Constants.SERVER_CONFIG_PREFIX+"ssl.key-store";
	public static String SSL_KEY_STORE_PASSWORD = Constants.SERVER_CONFIG_PREFIX+"ssl.key-store-password";
    public static String SSL_KEY_STORE_TYPE=Constants.SERVER_CONFIG_PREFIX+"ssl.key-store-type";
    public static String SSL_KEY_PASSWORD = Constants.SERVER_CONFIG_PREFIX+"ssl.key-password";
   
}
