package com.wibe.backend.dto;

import com.wibe.backend.entities.QueryResults.WibeObj;
import com.wibe.backend.entities.models.Wibe;

public class WibeDTO {
	
	private long wibeId;
	
	private String thumbnail;
	
	private String url;
	
	private long uploaderId;
	
	private long numViews;
	
	private long uploadedAt;
	
	private double latitude;
	
	private double longitude;
	
	private String uploaderName;
	
	private String uploaderPicUrl;
	
	private boolean isPrivate;
	
	private int height;
	
	private int width;
	
	public WibeDTO(Wibe wibe){
		this.setWibeId(wibe.getWibeId());
		this.setThumbnail(wibe.getThumbnail());
		this.url = wibe.getUrl();
		this.setHeight(wibe.getHeight());
		this.setWidth(wibe.getWidth());
		this.uploaderId = wibe.getUploaderId();
		this.uploaderName = wibe.getUploader() == null ? null: wibe.getUploader().getUsername();
		this.uploaderPicUrl = wibe.getUploader() == null ? null: wibe.getUploader().getImgUrl();
		this.uploadedAt = wibe.getUploadedAt() == null ? 0: wibe.getUploadedAt().getTime();
		this.latitude = wibe.getLatitude();
		this.longitude = wibe.getLongitude();
		this.numViews = wibe.getViewedBy() == null ? 0: wibe.getViewedBy().size();
		this.isPrivate = wibe.isIsPrivate();
	}
	
	public WibeDTO (WibeObj w){
		this.setWibeId(w.getWibe().getWibeId());
		this.setThumbnail(w.getWibe().getThumbnail());
		this.url = w.getWibe().getUrl();
		this.setHeight(w.getWibe().getHeight());
		this.setWidth(w.getWibe().getWidth());
		this.uploaderId = w.getUser().getUserId();
		this.uploaderName = w.getUser().getUsername();
		this.uploaderPicUrl = w.getUser().getImgUrl();
		this.uploadedAt = w.getWibe().getUploadedAt().getTime();
		this.latitude = w.getWibe().getLatitude();
		this.longitude = w.getWibe().getLongitude();
		this.numViews = w.getWibe().getNumViews();
		this.isPrivate = w.getWibe().isIsPrivate();
	}
	
	public WibeDTO (WibeInfoDTO wibe){
		this.setWibeId(wibe.getWibeId());
		this.setThumbnail(wibe.getThumbnail());
		this.url = wibe.getUrl();
		this.setHeight(wibe.getHeight());
		this.setWidth(wibe.getWidth());
		this.uploaderId = wibe.getUploaderId();
		this.uploaderName = wibe.getUploaderName();
		this.uploaderPicUrl = wibe.getUploaderPicUrl();
		this.uploadedAt = wibe.getUploadedAt();
		this.latitude = wibe.getLatitude();
		this.longitude = wibe.getLongitude();
		this.numViews = wibe.getNumViews();
		this.isPrivate = wibe.isIsPrivate();
	}
	
	public WibeDTO(){
		
	}

	public long getWibeId() {
		return wibeId;
	}

	public void setWibeId(long wibeId) {
		this.wibeId = wibeId;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public long getUploaderId() {
		return uploaderId;
	}

	public void setUploaderId(long uploaderId) {
		this.uploaderId = uploaderId;
	}

	public String getUploaderName() {
		return uploaderName;
	}

	public void setUploaderName(String uploaderName) {
		this.uploaderName = uploaderName;
	}

	public String getUploaderPicUrl() {
		return uploaderPicUrl;
	}

	public void setUploaderPicUrl(String uploaderImg) {
		this.uploaderPicUrl = uploaderImg;
	}

	public long getNumViews() {
		return numViews;
	}

	public void setNumViews(long numViews) {
		this.numViews = numViews;
	}

	public long getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(long uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
