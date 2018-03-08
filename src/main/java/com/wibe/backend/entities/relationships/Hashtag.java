package com.wibe.backend.entities.relationships;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.models.Tag;
import com.wibe.backend.entities.models.Wibe;

import java.util.Date;

@RelationshipEntity(type = "HASHTAG")
public class Hashtag {
	
	@GraphId Long id;
	
	@StartNode
	private Wibe wibe;
	
	@EndNode
	private Tag tag;
	
	@DateLong
	private Date time;

	public Wibe getWibe() {
		return wibe;
	}

	public void setWibe(Wibe wibe) {
		this.wibe = wibe;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Date getDate() {
		return time;
	}

	public void setDate(Date time) {
		this.time = time;
	}

}
