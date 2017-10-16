package com.minitwitter.model;

public class Pair {
    String userId;
    String followerId;
    public Pair(String userId, String followerId) {
    	this.userId = userId;
    	this.followerId = followerId;
    }
    public String getUserId() {
    	return this.userId;
    }
    public String getFollowerId() {
    	return this.followerId;
    }
}
