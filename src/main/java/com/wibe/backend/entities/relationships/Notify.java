package com.wibe.backend.entities.relationships;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.models.User;

import java.util.Date;

@RelationshipEntity(type = "NOTIFY")
public class Notify {
	
	@GraphId long id;
	
	@StartNode
	private User start;
	
	@EndNode
	private User end;
	
	@DateLong
	private Date time;

	private String type;
	
	private long wid;
	
	private String wibeThumbnail;
	
	private String messsage;
	
	private long uid;
	
	public User getStart() {
		return start;
	}

	public void setStart(User start) {
		this.start = start;
	}

	public User getEnd() {
		return end;
	}

	public void setEnd(User end) {
		this.end = end;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getWid() {
		return wid;
	}

	public void setWid(long wid) {
		this.wid = wid;
	}

	public String getMesssage() {
		return messsage;
	}

	public void setMesssage(String messsage) {
		this.messsage = messsage;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getWibeThumbnail() {
		return wibeThumbnail;
	}

	public void setWibeThumbnail(String wibeThumbnail) {
		this.wibeThumbnail = wibeThumbnail;
	}

}
