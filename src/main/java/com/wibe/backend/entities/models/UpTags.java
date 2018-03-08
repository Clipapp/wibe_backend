package com.wibe.backend.entities.models;

import java.util.List;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class UpTags {
	
	@GraphId
	private Long id;
	
	private List<String> tags;
		
	public UpTags(){
		
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
