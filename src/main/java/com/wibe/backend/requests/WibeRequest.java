package com.wibe.backend.requests;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.models.Wibe;

public class WibeRequest {

	private Long wibeId;
	
	private long numViews;
	
	private boolean deleted;
	
	private boolean approved;
	
	private boolean isPrivate;
	
	private String language;
	
	private long whatsAppShare;
	
	private boolean commentsRestricted;
	
	private double[] bbox;
	
	private long gtype;
	
	private String url;
	
	private String thumbnail;
	
	private double latitude;
	
	private double longitude;
	
	private boolean isFeatured;
	
	private Long uploaderId;
	
	private String ext;
	
	private int height;
	
	private int width;
	
	private List<String> categories;
	
	private List<String> tags;
	
	private String trackName;
	
	private String trackArtist;
	
	private String trackGenre;
	
	private String description;
	
	private long shareCount;
	
	private long downloadCount;
	
	private long approvedAt;
	
	private long lockedTill;
	
	@DateLong
	private Date uploadedAt;
	
	public WibeRequest(){
		
	}
	
	public WibeRequest(Wibe wibe){
		for (Field f : this.getClass().getDeclaredFields()){
			try {
				Field s = wibe.getClass().getDeclaredField(f.getName());
				s.setAccessible(true);
				f.set(this, s.get(wibe));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (this.bbox == null || this.bbox.length == 0){
			this.bbox = new double[] {this.longitude, this.latitude, this.longitude, this.latitude};
		}
	}

	public Long getWibeId() {
		return wibeId;
	}

	public void setWibeId(Long wibeId) {
		this.wibeId = wibeId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isCommentsRestricted() {
		return commentsRestricted;
	}

	public void setCommentsRestricted(boolean commentsRestricted) {
		this.commentsRestricted = commentsRestricted;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
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

	public boolean isIsFeatured() {
		return isFeatured;
	}

	public void setIsFeatured(boolean isFeatured) {
		this.isFeatured = isFeatured;
	}

	public Long getUploaderId() {
		return uploaderId;
	}

	public void setUploaderId(Long uploaderId) {
		this.uploaderId = uploaderId;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
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

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getTrackArtist() {
		return trackArtist;
	}

	public void setTrackArtist(String trackArtist) {
		this.trackArtist = trackArtist;
	}

	public String getTrackGenre() {
		return trackGenre;
	}

	public void setTrackGenre(String trackGenre) {
		this.trackGenre = trackGenre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(Date uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public double[] getBbox() {
		return bbox;
	}

	public void setBbox(double[] bbox) {
		this.bbox = bbox;
	}

	public long getGtype() {
		return gtype;
	}

	public void setGtype(long gtype) {
		this.gtype = gtype;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public long getWhatsAppShare() {
		return whatsAppShare;
	}

	public void setWhatsAppShare(long whatsAppShare) {
		this.whatsAppShare = whatsAppShare;
	}

	public long getShareCount() {
		return shareCount;
	}

	public void setShareCount(long shareCount) {
		this.shareCount = shareCount;
	}

	public boolean isIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public long getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(long downloadCount) {
		this.downloadCount = downloadCount;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public long getNumViews() {
		return numViews;
	}

	public void setNumViews(long numViews) {
		this.numViews = numViews;
	}

	public long getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(long approvedAt) {
		this.approvedAt = approvedAt;
	}

	public long getLockedTill() {
		return lockedTill;
	}

	public void setLockedTill(long lockedTill) {
		this.lockedTill = lockedTill;
	}
}
