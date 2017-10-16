package com.minitwitter.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.minitwitter.model.People;
import com.minitwitter.model.Pair;

@Service
public class FollowerService {
	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

	public boolean followUser(String userId, String followerId) {
		if ((valiateUsers(userId)) && (valiateUsers(followerId))) {
			List<Integer> l = new ArrayList<>();
			jdbcTemplate.query("select max(id) id from followers", (rs, rowNum) -> new Integer(rs.getInt("id")))
					.forEach(i -> l.add(i));

			if (l.size() == 0) {
				l.add(1);
			}

			try {
				jdbcTemplate.update("INSERT INTO followers(id, person_id, follower_person_id) VALUES (?,?,?)",
						new Object[] { l.get(0) + 1, userId, followerId });
				logger.info("Inserted successfully userid : " + userId + ", followerId : " + followerId);
				return true;
			} catch (Exception e) {
				logger.info(String.format("Inserted unsuccessfully userid : %s, followerId : %s", userId, followerId));
				return false;
			}
		}
		logger.info("Inserted unsuccessfully userid : " + userId + ", followerId : " + followerId);
		return false;
	}

	public boolean unfollowUser(String userId, String followerId) {
		if ((valiateUsers(userId)) && (valiateUsers(followerId))) {
			try {
				jdbcTemplate.update("delete from followers where person_id = ? and follower_person_id = ?",
						new Object[] { userId, followerId });
				logger.info("Deleted successfully userid : " + userId + ", followerId : " + followerId);
				return true;
			} catch (Exception e) {
				logger.info("Not deleted userid : " + userId + "followerId : " + followerId);
				return false;
			}
		}
		return false;
	}

	private boolean valiateUsers(String userId) {
		List<People> l = new ArrayList<>();
		jdbcTemplate
				.query("select * from people where id = ?", new Object[] { userId },
						(rs, rowNum) -> new People(rs.getInt("id"), rs.getString("handle"), rs.getString("name")))
				.forEach(people -> l.add(people));
		if (l.size() == 1) {
			return true;
		}
		logger.info("User Id not validated : " + userId);
		return false;
	}

	public List<Pair> getPopularFollowerPair() {
		List<Pair> l = new ArrayList<>();

		jdbcTemplate.execute(
				"create or replace view userfollowers as (select person_id, count(follower_person_id) number from followers group by person_id)");
		List<String> p = new ArrayList<>();
		// getting all the people id of which we will find the popular follower
		jdbcTemplate.query("select id from people", (rs, rowNum) -> new String(rs.getString("id")))
				.forEach(s -> p.add(s));
		// for each user getting the popular follower
		for (String s : p) {
			String followerPersonId = "";
			// create view for each user with followerid, no of followers for
			// that user
			String t = "create or replace view tmpfollowers(follower_person_id, no_of_followers) as (select f.follower_person_id, v.number from followers f, userfollowers v "
					+ "where f.person_id = " + s + " and f.follower_person_id = v.person_id)";
			jdbcTemplate.execute(t);

			String sql = "select follower_person_id from tmpfollowers where no_of_followers = "
					+ "(select max(no_of_followers) from tmpfollowers)";
			jdbcTemplate.query(sql, (rs, rowNum) -> new String(rs.getString("follower_person_id"))
			// ).forEach(str -> logger.info(str.toString()));
			// ).forEach(str -> assign(str, followerPersonId));
			).stream().forEach(str -> {
				l.add(new Pair(s, str));
			});

		}

		return l;
	}

	private void assign(String s1, String s2) {
		s2 = s1;
	}
}
