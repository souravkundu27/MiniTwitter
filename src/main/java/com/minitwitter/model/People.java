package com.minitwitter.model;

public class People {
    int id;
    String handle;
    String name;
    public People(int i, String h, String n) {
    	id = i;
    	handle = h;
    	name = n;
    }
    public int getId() {
    	return this.id;
    }
    public String getHandle() {
    	return this.handle;
    }
    public String getName() {
    	return this.name;
    }
}
