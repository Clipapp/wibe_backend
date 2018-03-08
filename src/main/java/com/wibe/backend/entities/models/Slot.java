package com.wibe.backend.entities.models;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Slot {

	@GraphId
	private Long id;
	
	private int slot;
	
	private int pos;
	
	private long wid;
	
	private long page;
	
	private boolean active;
	
	private long viewLimit;
	
	public Slot(){
		
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public long getWid() {
		return wid;
	}

	public void setWid(long wid) {
		this.wid = wid;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getViewLimit() {
		return viewLimit;
	}

	public void setViewLimit(long viewLimit) {
		this.viewLimit = viewLimit;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	
}
