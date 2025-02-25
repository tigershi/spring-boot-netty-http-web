package io.netty.mvc.config;

/**
 * 
 * @author shihu
 * @Date 2019年5月10日
 *
 */
public interface Constants {
	public final static String SERVER_CONFIG_PREFIX = "netty.server.";
	public final static int SERVER_CONFIG_WORK_GROUP_DEFAULT_SIZE =0;
	public final static int SERVER_CONFIG_BOSS_GROUP_DEFAULT_SIZE =1;
	public final static String SERVER_CONFIG_GROUP_DEFAULT_TYPE ="auto";
	public final static String SERVER_CONFIG_GROUP_NIO_TYPE ="nio";
	public final static String SERVER_CONFIG_GROUP_EPOLL_TYPE ="epoll";
	public final static String SERVER_CONFIG_GROUP_IO_URING_TYPE ="io_uring";


	public final static String PATH_VAL_PREFIX = "{";
	public final static String PATH_VAL_SUFFIX = "}";
	public final static String PATH_REPLACE_STR = "{\\w}";
	public static final String BACKSLASH = "/";
	public static final String COMMA = ",";
	public static final String EMPTY = "";
	public static final String DOT = ".";
	public static final String DOUBLE_DOT = "..";
	public static final String UNDERLINE = "_";
	public static final String DASHLINE = "-";
	public static final String QUESTIONMARK = "?";
	public static final String COLON = ":";
	public static final String SPACING = " ";
	public static final String POUND = "#";
	public static final String AT = "@";
	public static final String REVERSE = "^";
	public static final String LEFT_PARENTHESIS = "(";
	public static final String RIGHT_PARENTHESIS = ")";
	public static final String ASTERISK = "*";
}
