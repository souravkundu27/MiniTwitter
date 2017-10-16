package com.minitwitter.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.minitwitter.model.Messages;
import com.minitwitter.model.People;

@Service
public class MessageService {
	@Autowired
    JdbcTemplate jdbcTemplate;
	//private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
	   
    public List<Messages> getMessages(String userId, String search) {
    	List<Messages> l = new ArrayList<>();
    	String sql = "select id, content from Messages m where content like '%" + search + "%' and (m.person_id = ? or m.person_id in "
    			     +"(select person_id from Followers where follower_person_id = ?))";
        jdbcTemplate.query(
                sql,
                new Object[] { userId, userId },
                (rs, rowNum) -> new Messages(rs.getInt("id"), rs.getString("content"))
        ).forEach(customer -> l.add(customer));
    	return l;
    }
    
    public List<People> getPeople(String userId) {
    	List<People> l = new ArrayList<>();
        jdbcTemplate.query(
                "select id, handle, name from People p where p.id in (select follower_person_id from Followers where person_id = ?) or p.id in (select person_id from Followers where follower_person_id = ?)",
                new Object[] { userId, userId },
                (rs, rowNum) -> new People(rs.getInt("id"), rs.getString("handle"), rs.getString("name"))
        ).forEach(people -> l.add(people));
    	return l;
    }
}
