package com.wibe.backend.entities.relationships;

import java.util.Date;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Wibe;

@RelationshipEntity(type = "DOWNLOADED")
public class Download {
	
	@GraphId long id;
	
	@StartNode
	private User user;
	
	@EndNode
	private Wibe wibe;
	
	private long count;
	
	@DateLong
	private Date lastDownloaded;

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

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Date getLastDownloaded() {
		return lastDownloaded;
	}

	public void setLastDownloaded(Date lastViewed) {
		this.lastDownloaded = lastViewed;
	}

}
