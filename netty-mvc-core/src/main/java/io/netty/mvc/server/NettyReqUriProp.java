package io.netty.mvc.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.netty.mvc.bind.NettyMvcException;
import io.netty.mvc.config.Constants;
import io.netty.mvc.config.NettyMvcExceptions;
import io.netty.mvc.utils.AntPathMatcher;

public class NettyReqUriProp {

	private static AntPathMatcher antPathMatcher  = new AntPathMatcher();
	//url Str
	private String urlStr;
	//is path val url
	private boolean pathVal;
	//path map to the invake method and Object
	private Entry<Method, Object> mapMethod;
	
	//the path value url count number
	private static int pathValURLCount = 0;
	//it doesn't have path value url count number
	private static int SingleUrlCount = 0;
	// min slash size prefix path's last slash index
	private static int minSlashIdx = 0;
	//prefix path slash count that include max slash 
	private static int maxSlashCount=0;
	
	
	
	
    public static boolean isPattern(String path) {
    	return antPathMatcher.isPattern(path);
    }
    
    public static String prefixPath(String path) {
    	int index = path.indexOf(Constants.PATH_VAL_PREFIX);
    	if(index > 0) {
    		return path.substring(0, index);
    	}else {
    		return path;
    	}
    	
    }
    
    
   
    public static String subPrePathCountChar(String strContent, char charStr, int count) {
		int len = strContent.length()-1;
		int indx;
		int numb =0;
		for(indx = 0; indx <len; indx++){
			if((strContent.charAt(indx) == charStr) && (++numb == count)) {
				return strContent.substring(0, indx+1);
			}
		}
		
		do {
			len --;
		}while(strContent.charAt(len) != '/');
			
		return strContent.substring(0, (len+1));
	}

    
    
    
    
	/*
	 * public static String getPrefixPathUrl(String url) { for(String prefix :
	 * prefixPathUrls) { if(url.startsWith(prefix)) { return prefix; } } return url;
	 * }
	 */
    
    public static synchronized NettyReqUriProp newInstance(String urlStr, boolean pathVal, Entry<Method, Object> mapMethod) throws NettyMvcException {
    	if(!urlStr.startsWith("/")) {
    		throw NettyMvcExceptions.URL_SLASH_START_ERR;
    	}
    	
    	return new NettyReqUriProp(urlStr, pathVal, mapMethod);
    }
    
    private NettyReqUriProp(String urlStr, boolean pathVal, Entry<Method, Object> mapMethod) {
    	this.urlStr = urlStr;
    	this.pathVal = pathVal;
    	this.mapMethod = mapMethod;
    	
    	if(pathVal) {
    		String prefix = prefixPath(urlStr);
    		int slashCount = countPrePathSlash(prefix);
    		if(slashCount > maxSlashCount) {
    			maxSlashCount = slashCount;
    		}
    		int lastslashIdx = prefix.lastIndexOf("/");
    		if(minSlashIdx == 0 || minSlashIdx>lastslashIdx) {
    			minSlashIdx= lastslashIdx;
    		}
    		
    		pathValURLCount++;
    	}else{
    		SingleUrlCount++;
    	}
    	
  
    	
    }


   public static  int countPrePathSlash(String prePath){
	   int numb =0;
	   int length = prePath.length();
	   for(int idx=0; idx < length; idx++) {
		   if(prePath.charAt(idx) == '/') {
				numb++;
			}
	   }
	   
	   return numb;
	  
   }
   
    public boolean pathMatch(String uri){
		if(pathVal) {
		   return antPathMatcher.match(urlStr, uri);
		}else {
			return uri.equals(this.urlStr);
		}
	}
    
	public Map<String, String> extractUriTemplateVariables(String uri){
		return antPathMatcher.extractUriTemplateVariables(urlStr, uri);
	}

	
	public String getUrlStr() {
		return urlStr;
	}
	public boolean isPathVal() {
		return pathVal;
	}
	public Entry<Method, Object> getMapMethod() {
		return mapMethod;
	}
	public void setMapMethod(Entry<Method, Object> mapMethod) {
		this.mapMethod = mapMethod;
	}

	
	public static int getPathValURLCount() {
		return pathValURLCount;
	}

	public static int getSingleUrlCount() {
		return SingleUrlCount;
	}

	public static int getMinSlashIdx() {
		return minSlashIdx;
	}

	public static int getMaxSlashCount() {
		return maxSlashCount;
	}

	

}
