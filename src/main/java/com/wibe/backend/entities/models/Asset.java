package com.wibe.backend.entities.models;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Asset {
	
	@GraphId Long id;
	
	private long assetId;
	
	private String assetType;
	
	private long weight;
	
	private String assetName;
	
	private boolean live;
	
	private String assetUrl;
	
	private String assetThumbnail;
	
	private long uploadedAt;
	
	public Asset(long assetId, String assetUrl, String assetThumbnail, String assetType, 
			String assetName, boolean live){
		this.assetId = assetId;
		this.assetUrl = assetUrl;
		this.assetThumbnail = assetThumbnail;
		this.assetType = assetType;
		this.assetName = assetName;
		this.live = live;
		this.uploadedAt = System.currentTimeMillis();
		this.weight = this.assetId*1000;
	}
	
	public Asset(){
		
	}

	public long getAssetId() {
		return assetId;
	}

	public void setAssetId(long assetId) {
		this.assetId = assetId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public String getAssetUrl() {
		return assetUrl;
	}

	public void setAssetUrl(String assetUrl) {
		this.assetUrl = assetUrl;
	}

	public String getAssetThumbnail() {
		return assetThumbnail;
	}

	public void setAssetThumbnail(String assetThumbnail) {
		this.assetThumbnail = assetThumbnail;
	}

	public long getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(long uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

}
