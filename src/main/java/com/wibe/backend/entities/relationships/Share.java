package com.wibe.backend.entities.relationships;

import java.util.Date;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Wibe;

@RelationshipEntity(type = "SHARED")
public class Share {

	@GraphId long id;
	
	@StartNode
	private User user;
	
	@EndNode
	private Wibe wibe;
	
	private long wShares;
	
	private long oShares;
	
	@DateLong
	private Date lastShared;

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

	public long getOShares() {
		return oShares;
	}

	public void setOShares(long oShares) {
		this.oShares = oShares;
	}

	public Date getLastShared() {
		return lastShared;
	}

	public void setLastShared(Date lastShared) {
		this.lastShared = lastShared;
	}

	public long getwShares() {
		return wShares;
	}

	public void setwShares(long wShares) {
		this.wShares = wShares;
	}

}
