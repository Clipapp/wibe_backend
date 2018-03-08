package com.wibe.backend.entities.relationships;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Wibe;

import java.util.Date;

@RelationshipEntity(type = "COMMENT")
public class Comments {
	
	@GraphId Long id;
	
	private String commentId;
	
	@StartNode
	private User user;
	
	@EndNode
	private Wibe wibe;
	
	@DateLong
	private Date date;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

}
