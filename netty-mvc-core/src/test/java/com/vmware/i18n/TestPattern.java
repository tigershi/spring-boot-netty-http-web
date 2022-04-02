package com.vmware.i18n;

import io.netty.mvc.utils.AntPathMatcher;

public class TestPattern {
	
	private static AntPathMatcher apm = new AntPathMatcher();
	
	
	
	public static void main(String[] args) {
		System.out.println(apm.match("/abc/{test}/name/{size}", "/abc/dd/name/444"));
		System.out.println(apm.isPattern("/abc/dd/name/444"));
	}

}
