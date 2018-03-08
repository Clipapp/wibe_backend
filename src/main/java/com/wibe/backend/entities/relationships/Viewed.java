package com.wibe.backend.entities.relationships;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Wibe;

import java.util.Date;

@RelationshipEntity(type = "VIEWED")
public class Viewed {
	
	@GraphId long id;
	
	@StartNode
	private User user;
	
	@EndNode
	private Wibe wibe;
	
	private int count;
	
	@DateLong
	private Date lastViewed;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Wibe getWibe() {
		return wibe;
	}

	public void setWibe(Wibe wibe) {
		this.wibe = wibe;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Date getLastViewed() {
		return lastViewed;
	}

	public void setLastViewed(Date lastViewed) {
		this.lastViewed = lastViewed;
	}

}
