package com.minitwitter.model;

public class Messages {
    int id;
    String msg;
    public Messages(int i, String m) {
    	id = i;
    	msg = m;
    }
    public int getId() {
    	return this.id;
    }
    public String getMsg() {
    	return this.msg;
    }
}
