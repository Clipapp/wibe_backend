package com.wibe.backend.dto;

import java.util.List;

import com.wibe.backend.entities.models.Wibe;

public class WibeInfoDTO {

	private Long wibeId;
	
	private Long uploaderId;
	
	private boolean commentsRestricted;
	
	private String uploaderName;
	
	private String uploaderPicUrl;
	
	private String description;
	
	private int height;
	
	private int width;
	
	private String thumbnail;
	
	private List<String> categories;
	
	private List<String> tags;
	
	private String trackName;
	
	private String trackArtist;
	
	private String trackGenre;
	
	private String url;
	
	private int numLikes;
	
	private int numShares;
	
	private int numComments;
	
	private int numFavourites;
	
	private long numViews;
	
	private Long uploadedAt;
	
	private boolean liked;
	
	private boolean isFavourite;
	
	private boolean following;
	
	private double latitude;
	
	private double longitude;
	
	private long whatsAppShare;
	
	private long shareCount;
	
	private long downloadCount;
	
	private boolean isPrivate; 
	
	public WibeInfoDTO(){
		
	}
	
	public WibeInfoDTO(long wibeId, String thumbnail){
		this.wibeId = wibeId;
		this.thumbnail = thumbnail;
	}
	
	public WibeInfoDTO(Wibe wibe){
		this.wibeId = wibe.getWibeId();
		this.uploaderId = wibe.getUploaderId();
		this.uploaderName = wibe.getUploader() == null ? null: wibe.getUploader().getUsername();
		this.uploaderPicUrl = wibe.getUploader() == null ? null: wibe.getUploader().getImgUrl();
		this.categories = wibe.getCategories();
		this.tags = wibe.getTags();
		this.description = wibe.getDescription();
		this.thumbnail = wibe.getThumbnail();
		this.trackName = wibe.getTrackName();
		this.trackArtist = wibe.getTrackArtist();
		this.trackGenre = wibe.getTrackGenre();
		this.url = wibe.getUrl();
		this.numComments = wibe.getComments() == null ? 0: wibe.getComments().size();
		this.numLikes = wibe.getLikedBy() == null ? 0: wibe.getLikedBy().size();
		this.numShares = wibe.getSharedBy() == null ? 0: wibe.getSharedBy().size();
		this.numFavourites = wibe.getFavouriteOf() == null ? 0: wibe.getFavouriteOf().size();
		this.numViews = wibe.getNumViews();
		this.uploadedAt = wibe.getUploadedAt() == null ? 0: wibe.getUploadedAt().getTime();
		this.height = wibe.getHeight();
		this.width = wibe.getWidth();
		this.latitude = wibe.getLatitude();
		this.longitude = wibe.getLongitude();
		this.whatsAppShare = wibe.getWhatsAppShare();
		this.shareCount = wibe.getShareCount();
		this.downloadCount = wibe.getDownloadCount();
		this.isPrivate = wibe.isIsPrivate();
	}
	
	public WibeInfoDTO(Wibe wibe, boolean liked, boolean isFavourite, boolean following){
		this(wibe);
		this.liked = liked;
		this.isFavourite = isFavourite;
		this.following = following;
	}

	public Long getWibeId() {
		return wibeId;
	}

	public void setWibeId(Long wibeId) {
		this.wibeId = wibeId;
	}

	public Long getUploaderId() {
		return uploaderId;
	}

	public void setUploaderId(Long uploaderId) {
		this.uploaderId = uploaderId;
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

	public String getUploaderName() {
		return uploaderName;
	}

	public void setUploaderName(String uploaderName) {
		this.uploaderName = uploaderName;
	}

	public int getNumLikes() {
		return numLikes;
	}

	public void setNumLikes(int numLikes) {
		this.numLikes = numLikes;
	}

	public int getNumShares() {
		return numShares;
	}

	public void setNumShares(int numShares) {
		this.numShares = numShares;
	}

	public int getNumComments() {
		return numComments;
	}

	public void setNumComments(int numComments) {
		this.numComments = numComments;
	}

	public int getNumFavourites() {
		return numFavourites;
	}

	public void setNumFavourites(int numFavourites) {
		this.numFavourites = numFavourites;
	}

	public long getNumViews() {
		return numViews;
	}

	public void setNumViews(long numViews) {
		this.numViews = numViews;
	}

	public Long getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(Long uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUploaderPicUrl() {
		return uploaderPicUrl;
	}

	public void setUploaderPicUrl(String uploaderPicUrl) {
		this.uploaderPicUrl = uploaderPicUrl;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public boolean isFavourite() {
		return isFavourite;
	}

	public void setFavourite(boolean isFavourite) {
		this.isFavourite = isFavourite;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public boolean isFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public boolean isCommentsRestricted() {
		return commentsRestricted;
	}

	public void setCommentsRestricted(boolean commentsRestricted) {
		this.commentsRestricted = commentsRestricted;
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
	
	
}
