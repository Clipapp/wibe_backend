package com.wibe.backend.entities.models;

import java.util.List;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class WibeList {

	@GraphId Long id;
	
	private long listId;
	
	private List<Long> wibes;
	
	public WibeList(){
		
	}

	public long getListId() {
		return listId;
	}

	public void setListId(long listId) {
		this.listId = listId;
	}

	public List<Long> getWibes() {
		return wibes;
	}

	public void setWibes(List<Long> wibes) {
		this.wibes = wibes;
	}
}
