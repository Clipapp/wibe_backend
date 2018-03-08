package com.wibe.backend.entities.relationships;

import java.util.Date;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.models.User;

@RelationshipEntity(type = "BLOCKED")
public class Block {
	
	@GraphId long id;
	
	@StartNode
	private User blocker;
	
	@EndNode
	private User blocked;

	@DateLong
	private Date time;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public User getBlocked() {
		return blocked;
	}

	public void setBlocked(User blocked) {
		this.blocked = blocked;
	}

	public User getBlocker() {
		return blocker;
	}

	public void setBlocker(User blocker) {
		this.blocker = blocker;
	}
}
