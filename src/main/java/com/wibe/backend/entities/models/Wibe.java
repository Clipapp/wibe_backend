package com.wibe.backend.entities.models;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.relationships.Comments;
import com.wibe.backend.responses.TrackInfo;

//Don't forget to update WibeRequest after adding new variables in this class
@NodeEntity
public class Wibe {
	
	@GraphId Long id;
	
	private Long wibeId;
	
	private long numViews;
	
	private double[] bbox;
	
	private boolean isPrivate;
	
	private boolean approved = false;
	
	private long gtype;
	
	private long whatsAppShare;
	
	private String language;
	
	private boolean deleted = true;
	
	private boolean commentsRestricted = false;
	
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
	
	public long tmp;
	
	private long approvedAt;
	
	private long lockedTill;
	
	@DateLong
	private Date uploadedAt;
	
	@Relationship(type = "HASHTAG")
	private List<Tag> tag;
	
	@Relationship(type = "CATEGORY")
	private List<Category> category;
	
	@Relationship(type="TAGGED")
	private Set<User> tagged;
	
	@Relationship(type = "FAVOURITE", direction = Relationship.INCOMING)
	private Set<User> favouriteOf;
	
	@Relationship(type = "LIKED", direction = Relationship.INCOMING)
	private Set<User> likedBy;
	
	@Relationship(type = "COMMENT", direction = Relationship.INCOMING)
	private Set<Comments> comments;
	
	@Relationship(type = "UPLOAD", direction = Relationship.INCOMING)
	private User uploader;
	
	@Relationship(type = "SHARED", direction = Relationship.INCOMING)
	private Set<User> sharedBy;
	
	@Relationship(type="VIEWED", direction = Relationship.INCOMING)
	private Set<User> viewedBy;
	
	@Relationship(type="DOWNLOADED", direction = Relationship.INCOMING)
	private Set<User> downloadedBy;
	
	public Wibe(){
		
	}
	
	
	public Wibe(Long wibeId, Long uploaderId, String description, String url,
			List<String> categories, List<String> tags, TrackInfo track, Date uploadedAt, 
			String ext, double lat, double lon, String thumbnail, int height, int width,
			boolean isPrivate){
		this.uploaderId = uploaderId;
		this.description = description;
		this.url = url;
		this.wibeId = wibeId;
		this.categories = categories;
		this.tags = tags;
		this.trackName= track.getName();
		this.trackArtist = track.getArtist();
		this.trackGenre = track.getGenre();
		this.uploadedAt = uploadedAt;
		this.ext = ext;
		this.latitude = lat;
		this.longitude = lon;
		this.thumbnail = thumbnail;
		this.height = height;
		this.width = width;
		this.isPrivate = isPrivate;
	}
	
	public void setWibeId(Long wibeId){
		this.wibeId = wibeId;
	}
	
	public Long getWibeId(){
		return this.wibeId;
	}
	
	public void setUrl(String url){
		this.url= url;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public void setUploaderId(Long uploaderId){
		this.uploaderId = uploaderId;
	}
	
	public Long getUploaderId(){
		return this.uploaderId;
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
	
	public void setUploadedAt(Date uploadedAt){
		this.uploadedAt = uploadedAt;
	}
	
	public Date getUploadedAt(){
		return this.uploadedAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<User> getTagged() {
		return tagged;
	}

	public void setTagged(Set<User> tagged) {
		this.tagged = tagged;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Set<User> getFavouriteOf() {
		return favouriteOf;
	}

	public void setFavouriteOf(Set<User> favouriteOf) {
		this.favouriteOf = favouriteOf;
	}

	public Set<User> getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(Set<User> likedBy) {
		this.likedBy = likedBy;
	}

	public Set<Comments> getComments() {
		return comments;
	}

	public void setComments(Set<Comments> comments) {
		this.comments = comments;
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


	public User getUploader() {
		return uploader;
	}


	public void setUploader(User uploader) {
		this.uploader = uploader;
	}
	
	public Set<User> getSharedBy(){
		return this.sharedBy;
	}
	
	public void setSharedBy(Set<User> sharedBy){
		this.sharedBy = sharedBy;
	}


	public Set<User> getViewedBy() {
		return viewedBy;
	}


	public void setViewedBy(Set<User> viewedBy) {
		this.viewedBy = viewedBy;
	}


	public boolean isIsFeatured() {
		return isFeatured;
	}


	public void setIsFeatured(boolean isFeatured) {
		this.isFeatured = isFeatured;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double lat) {
		this.latitude = lat;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double lon) {
		this.longitude = lon;
	}


	public List<Tag> getTag() {
		return tag;
	}


	public void setTag(List<Tag> tag) {
		this.tag = tag;
	}


	public List<Category> getCategory() {
		return category;
	}


	public void setCategory(List<Category> category) {
		this.category = category;
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


	public boolean isCommentsRestricted() {
		return commentsRestricted;
	}


	public void setCommentsRestricted(boolean commentsRestricted) {
		this.commentsRestricted = commentsRestricted;
	}


	public boolean isDeleted() {
		return deleted;
	}


	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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


	public Set<User> getDownloadedBy() {
		return downloadedBy;
	}


	public void setDownloadedBy(Set<User> downloadedBy) {
		this.downloadedBy = downloadedBy;
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
