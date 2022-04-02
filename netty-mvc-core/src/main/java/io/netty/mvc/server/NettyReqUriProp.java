package io.netty.mvc.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.netty.mvc.config.Constants;
import io.netty.mvc.utils.AntPathMatcher;

public class NettyReqUriProp {

	private static AntPathMatcher antPathMatcher  = new AntPathMatcher();
	private String urlStr;
	private boolean pathVal;
	private Entry<Method, Object> mapMethod;
	private final static List<String> prefixPathUrls = new ArrayList<String>();
	private static int maxIdx;
	private static int minIdx;
	
	
	
	
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
    
    public static String getPrefixPathUrl(String url) {
    	for(String prefix : prefixPathUrls) {
    		if(url.startsWith(prefix)) {
    			return prefix;
    		}
    	}
		return url;
    }
    
    public static synchronized NettyReqUriProp newInstance(String urlStr, boolean pathVal, Entry<Method, Object> mapMethod) {
    	return new NettyReqUriProp(urlStr, pathVal, mapMethod);
    }
    
    private NettyReqUriProp(String urlStr, boolean pathVal, Entry<Method, Object> mapMethod) {
    	this.urlStr = urlStr;
    	this.pathVal = pathVal;
    	this.mapMethod = mapMethod;
    	String prefix = prefixPath(urlStr);
    	if(pathVal && !prefixPathUrls.contains(prefix)) {
    		prefixPathUrls.add(prefix);
    		Collections.sort(prefixPathUrls,  new Comparator<String>(){

    			@Override
    			public int compare(String str1, String str2) {
    				// TODO Auto-generated method stub
    				return str1.length()-str2.length();
    			}
    			
    		});
    		
    		minIdx = prefixPathUrls.get(0).length();
    		maxIdx = prefixPathUrls.get(prefixPathUrls.size()-1).length();
    		
    	}
    	
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

	public static int getMinIdx() {
		return minIdx;
	}

	public static int getMaxIdx() {
		return maxIdx;
	}

	

}
