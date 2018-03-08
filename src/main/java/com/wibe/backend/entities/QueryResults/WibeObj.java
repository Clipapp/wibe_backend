package com.wibe.backend.entities.QueryResults;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Wibe;

@QueryResult
public class WibeObj {
	
	private Wibe wibe;
	
	private User user;

	public WibeObj(){
		
	}
	
	public Wibe getWibe() {
		return wibe;
	}

	public void setWibe(Wibe wibe) {
		this.wibe = wibe;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
