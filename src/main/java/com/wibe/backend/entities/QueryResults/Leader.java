package com.wibe.backend.entities.QueryResults;

import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class Leader {
	
	private long userId;
	
	private String username;
	
	private String fullname;
	
	private String imgUrl;
	
	private long count;
	
	private boolean subscribed;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String name) {
		this.fullname = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}

}
