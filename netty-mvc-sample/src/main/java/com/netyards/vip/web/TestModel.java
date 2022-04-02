package com.netyards.vip.web;

import java.io.Serializable;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public class TestModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4263287471232531583L;
	
	
	private int id;
	private String name;
	private String test;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}

}
