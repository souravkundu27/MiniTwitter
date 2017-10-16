package com.minitwitter.model;

public class Followers {
    int id;
    int personId;
    int followerPersonId;
    public Followers(int id, int personId, int followerPersonId) {
    	this.id = id;
    	this.personId = personId;
    	this.followerPersonId = followerPersonId;
    }
    public int getId() {
    	return this.id;
    }
    public int getPersonId() {
    	return this.personId;
    }
    public int getFollowerPersonId() {
    	return this.followerPersonId;
    }
}
