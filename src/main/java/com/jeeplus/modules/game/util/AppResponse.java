package com.jeeplus.modules.game.util;

public class AppResponse<T> {
	private int state;
	private String msg;
	private T data;
	
	public AppResponse() {
		// TODO Auto-generated constructor stub
	}
	public AppResponse(int state,String msg,T data) {
		// TODO Auto-generated constructor stub
		this.state = state;
		this.msg = msg;
		this.data = data;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
