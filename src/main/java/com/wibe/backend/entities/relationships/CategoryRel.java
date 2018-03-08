package com.wibe.backend.entities.relationships;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.models.Category;
import com.wibe.backend.entities.models.Wibe;

import java.util.Date;

@RelationshipEntity(type = "CATEGORY")
public class CategoryRel {
	
	@GraphId long id;
	
	@StartNode
	private Wibe wibe;
	
	@EndNode
	private Category cat;
	
	@DateLong
	private Date time;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Category getCat() {
		return cat;
	}

	public void setCat(Category cat) {
		this.cat = cat;
	}

	public Wibe getWibe() {
		return wibe;
	}

	public void setWibe(Wibe wibe) {
		this.wibe = wibe;
	}

}
