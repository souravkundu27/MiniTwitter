package com.minitwitter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.minitwitter.model.Messages;
import com.minitwitter.model.Pair;
import com.minitwitter.model.People;
import com.minitwitter.service.*;

@Controller
public class MessageController {
	    @Autowired
        private MessageService messageService;
	    @Autowired
        private FollowerService followerService;
	    
	    //An endpoint to read the message list for the current user (as identified by their HTTP Basic authentication credentials). 
	    //Include messages they have sent and messages sent by users they follow. Support a “search=” parameter 
	    //that can be used to further filter messages based on keyword.
	    @RequestMapping(value="users/{userId}/messages", method=RequestMethod.GET)
	    public @ResponseBody List<Messages> getMessages(@PathVariable final String userId, @RequestParam final String search) {
	    	return messageService.getMessages(userId, search);
	    }
	    
	    //Endpoints to get the list of people the user is following as well as the followers of the user.
	    @RequestMapping(value="users/{userId}/people", method=RequestMethod.GET)
	    public @ResponseBody List<People> getPeople(@PathVariable final String userId) {
	    	return messageService.getPeople(userId);
	    }
	    
	    //An endpoint to start following another user.
	    @RequestMapping(value="user/{userId}/follower/{followerId}", method=RequestMethod.POST)
	    public @ResponseBody String followUser(@PathVariable final String userId, @PathVariable final String followerId) {
	    	if (followerService.followUser(userId, followerId)) {
	    		return String.format("Follow user record is inserted successfully for userId : %s, followerId : %s",userId, followerId);
	    	}
	    	return String.format("Follow user record is not inserted successfully for userId : %s, followerId : %s",userId, followerId);
	    }
	    
	    //An endpoint to unfollow another user. 
	    @RequestMapping(value="user/{userId}/follower/{followerId}", method=RequestMethod.DELETE)
	    public @ResponseBody String unfollowUser(@PathVariable final String userId, @PathVariable final String followerId) {
	    	if (followerService.unfollowUser(userId, followerId)) {
	    		return String.format("Follow user record is deleted successfully for userId : %s, followerId : %s",userId, followerId);
	    	}
	    	return String.format("Follow user record is not deleted successfully for userId : %s, followerId : %s",userId, followerId);
	    }
	    
	    //An endpoint that returns a list of all users, paired with their most "popular" follower.
	    @RequestMapping(value="popularfollower", method=RequestMethod.POST)
	    public @ResponseBody List<Pair> popularFollowerPair() {
	    	return followerService.getPopularFollowerPair();
	    }
}
