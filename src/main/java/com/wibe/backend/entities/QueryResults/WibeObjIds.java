package com.wibe.backend.entities.QueryResults;

import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class WibeObjIds {
	
	private long wibeId;
	
	private long uploaderId;
	
	public WibeObjIds(){
		
	}

	public long getWibeId() {
		return wibeId;
	}

	public void setWibeId(long wibeId) {
		this.wibeId = wibeId;
	}

	public long getUploaderId() {
		return uploaderId;
	}

	public void setUploaderId(long uploaderId) {
		this.uploaderId = uploaderId;
	}

}
